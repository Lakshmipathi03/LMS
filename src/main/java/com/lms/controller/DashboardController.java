package com.lms.controller;

import com.lms.dto.DashboardDto;
import com.lms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardDto.AdminDashboard> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<DashboardDto.InstructorDashboard> getInstructorDashboard() {
        return ResponseEntity.ok(dashboardService.getInstructorDashboard());
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<DashboardDto.StudentDashboard> getStudentDashboard() {
        return ResponseEntity.ok(dashboardService.getStudentDashboard());
    }

    @GetMapping("/instructor/course/{courseId}/stats")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<DashboardDto.CourseStats> getCourseStats(@PathVariable Long courseId) {
        return ResponseEntity.ok(dashboardService.getCourseStats(courseId));
    }

    @GetMapping("/student/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<DashboardDto.StudentProgress> getStudentProgress() {
        return ResponseEntity.ok(dashboardService.getStudentProgress());
    }
}
