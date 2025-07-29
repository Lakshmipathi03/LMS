package com.lms.repository;

import com.lms.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    List<Submission> findByAssignmentId(Long assignmentId);
    
    List<Submission> findByStudentId(Long studentId);
    
    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
    
    @Query("SELECT s FROM Submission s WHERE s.assignment.id = :assignmentId AND s.grade IS NULL")
    List<Submission> findUngradedSubmissions(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT s FROM Submission s " +
           "WHERE s.assignment.course.instructor.id = :instructorId " +
           "AND s.grade IS NULL")
    List<Submission> findPendingGradingByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT AVG(s.grade) FROM Submission s " +
           "WHERE s.assignment.id = :assignmentId AND s.grade IS NOT NULL")
    Double getAverageGrade(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT s FROM Submission s " +
           "WHERE s.student.id = :studentId " +
           "ORDER BY s.submittedAt DESC")
    List<Submission> findRecentSubmissionsByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT COUNT(s) FROM Submission s " +
           "WHERE s.assignment.id = :assignmentId AND s.grade IS NOT NULL")
    long countGradedSubmissions(@Param("assignmentId") Long assignmentId);
}
