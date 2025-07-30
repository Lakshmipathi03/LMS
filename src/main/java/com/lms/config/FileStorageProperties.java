package com.lms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String allowedTypes;
    private int maxFilenameLength;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public List<String> getAllowedTypes() {
        return Arrays.asList(allowedTypes.split(","));
    }

    public void setAllowedTypes(String allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    public int getMaxFilenameLength() {
        return maxFilenameLength;
    }

    public void setMaxFilenameLength(int maxFilenameLength) {
        this.maxFilenameLength = maxFilenameLength;
    }
}
