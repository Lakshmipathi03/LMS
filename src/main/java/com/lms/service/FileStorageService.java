package com.lms.service;

import com.lms.config.FileStorageProperties;
import com.lms.exception.FileStorageException;
import com.lms.exception.InvalidFileTypeException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String subdirectory) {
        validateFile(file);

        try {
            Path targetLocation = createTargetLocation(subdirectory);
            String fileName = generateUniqueFileName(file);
            Path filePath = targetLocation.resolve(fileName);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName, String subdirectory) {
        try {
            Path filePath = this.fileStorageLocation.resolve(subdirectory).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    public void deleteFile(String fileName, String subdirectory) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(subdirectory);
            Path filePath = targetLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file. Please try again!", ex);
        }
    }

    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }

        // Validate file name length
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.length() > fileStorageProperties.getMaxFilenameLength()) {
            throw new FileStorageException("File name is too long. Maximum length allowed is " 
                    + fileStorageProperties.getMaxFilenameLength());
        }

        // Validate file extension
        String fileExtension = getFileExtension(fileName);
        if (!fileStorageProperties.getAllowedTypes().contains(fileExtension.toLowerCase())) {
            throw new InvalidFileTypeException("File type not allowed. Allowed types are: " 
                    + String.join(", ", fileStorageProperties.getAllowedTypes()));
        }

        // Check for directory traversal security vulnerability
        if (fileName.contains("..")) {
            throw new FileStorageException("Cannot store file with relative path outside current directory " 
                    + fileName);
        }
    }

    private Path createTargetLocation(String subdirectory) throws IOException {
        Path targetLocation = this.fileStorageLocation.resolve(subdirectory);
        Files.createDirectories(targetLocation);
        return targetLocation;
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + fileExtension;
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            throw new FileStorageException("Invalid file name: " + fileName);
        }
    }
}
