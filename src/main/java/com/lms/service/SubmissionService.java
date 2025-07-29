package com.lms.service;

import com.lms.model.Submission;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface SubmissionService {
    Submission submitAssignment(Long assignmentId, Long studentId, MultipartFile file);
    Submission gradeSubmission(Long submissionId, Double grade, String feedback);
    Optional<Submission> findById(Long submissionId);
    List<Submission> findSubmissionsByAssignment(Long assignmentId);
    List<Submission> findSubmissionsByStudent(Long studentId);
    List<Submission> findUngradedSubmissions(Long assignmentId);
    Double getAverageGrade(Long assignmentId);
    boolean hasStudentSubmitted(Long assignmentId, Long studentId);
    void deleteSubmission(Long submissionId);
    byte[] downloadSubmissionFile(Long submissionId);
}
