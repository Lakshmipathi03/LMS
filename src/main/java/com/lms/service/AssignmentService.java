package com.lms.service;

import com.lms.model.Assignment;
import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    Assignment createAssignment(Assignment assignment);
    Assignment updateAssignment(Assignment assignment);
    void deleteAssignment(Long assignmentId);
    Optional<Assignment> findById(Long assignmentId);
    List<Assignment> findAllAssignments();
    List<Assignment> findAssignmentsByCourse(Long courseId);
    List<Assignment> findUpcomingAssignments(Long courseId);
    List<Assignment> findOverdueAssignments(Long userId);
    long getSubmissionCount(Long assignmentId);
    boolean isDeadlinePassed(Long assignmentId);
    void extendDeadline(Long assignmentId, int days);
}
