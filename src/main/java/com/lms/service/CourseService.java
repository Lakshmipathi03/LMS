package com.lms.service;

import com.lms.model.Course;
import com.lms.model.Enrollment;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);
    Course updateCourse(Course course);
    void deleteCourse(Long courseId);
    Optional<Course> findById(Long courseId);
    List<Course> findAllCourses();
    List<Course> findCoursesByInstructor(Long instructorId);
    List<Course> searchCourses(String searchTerm);
    Enrollment enrollStudent(Long courseId, Long studentId);
    void unenrollStudent(Long courseId, Long studentId);
    List<Enrollment> findEnrollmentsByCourse(Long courseId);
    List<Course> findEnrolledCourses(Long studentId);
    boolean isStudentEnrolled(Long courseId, Long studentId);
    long getEnrollmentCount(Long courseId);
}
