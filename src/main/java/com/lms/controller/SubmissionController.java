package com.lms.controller;

import com.lms.dto.SubmissionDto;
import com.lms.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<SubmissionDto> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile file) {
        // Store file and get the generated filename
        String fileName = fileStorageService.storeFile(file, "submissions/" + assignmentId);
        return ResponseEntity.ok(submissionService.submitAssignment(assignmentId, fileName));
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<SubmissionDto>> getAssignmentSubmissions(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getAssignmentSubmissions(assignmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadSubmission(@PathVariable Long id) {
        SubmissionDto submission = submissionService.getSubmissionById(id);
        
        // Security check: Verify if the current user has access to this submission
        if (!submissionService.hasAccessToSubmission(id)) {
            throw new ForbiddenException("You don't have permission to access this submission");
        }

        Resource resource = fileStorageService.loadFileAsResource(
            submission.getFileName(), 
            "submissions/" + submission.getAssignmentId()
        );

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
        } catch (IOException ex) {
            // Fallback to default content type if type cannot be determined
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + submission.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}/file")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> deleteSubmissionFile(@PathVariable Long id) {
        SubmissionDto submission = submissionService.getSubmissionById(id);
        fileStorageService.deleteFile(
            submission.getFileName(), 
            "submissions/" + submission.getAssignmentId()
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/grade")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<SubmissionDto> gradeSubmission(
            @PathVariable Long id,
            @RequestParam Double grade,
            @RequestParam(required = false) String feedback) {
        return ResponseEntity.ok(submissionService.gradeSubmission(id, grade, feedback));
    }

    @GetMapping("/student/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<SubmissionDto>> getStudentCourseSubmissions(@PathVariable Long courseId) {
        return ResponseEntity.ok(submissionService.getStudentCourseSubmissions(courseId));
    }
}
