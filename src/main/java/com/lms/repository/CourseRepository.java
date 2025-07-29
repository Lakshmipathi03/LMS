package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructor(User instructor);
    
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT c FROM Course c " +
           "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Course> searchCourses(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Course c " +
           "LEFT JOIN c.enrollments e " +
           "GROUP BY c.id " +
           "ORDER BY COUNT(e) DESC")
    List<Course> findMostPopularCourses();
    
    @Query("SELECT COUNT(e) FROM Course c JOIN c.enrollments e WHERE c.id = :courseId")
    long getEnrollmentCount(@Param("courseId") Long courseId);
}
