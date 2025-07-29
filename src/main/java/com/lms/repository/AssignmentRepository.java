package com.lms.repository;

import com.lms.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByCourseId(Long courseId);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.deadline > :now")
    List<Assignment> findUpcomingAssignmentsByCourse(
        @Param("courseId") Long courseId,
        @Param("now") LocalDateTime now
    );
    
    @Query("SELECT a FROM Assignment a WHERE a.deadline < :now AND " +
           "NOT EXISTS (SELECT s FROM Submission s WHERE s.assignment = a AND s.student.id = :userId)")
    List<Assignment> findOverdueAssignmentsForUser(
        @Param("userId") Long userId,
        @Param("now") LocalDateTime now
    );
    
    @Query("SELECT a FROM Assignment a " +
           "WHERE a.course.instructor.id = :instructorId " +
           "ORDER BY a.deadline DESC")
    List<Assignment> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(s) FROM Submission s WHERE s.assignment.id = :assignmentId")
    long getSubmissionCount(@Param("assignmentId") Long assignmentId);
}
