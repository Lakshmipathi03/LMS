package com.lms.controller;

import com.lms.dto.AssignmentDto;
import com.lms.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssignmentDto>> getCourseAssignments(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentService.getCourseAssignments(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDto> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<AssignmentDto> createAssignment(
            @PathVariable Long courseId,
            @Valid @RequestBody AssignmentDto assignmentDto) {
        return ResponseEntity.ok(assignmentService.createAssignment(courseId, assignmentDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<AssignmentDto> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentDto assignmentDto) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, assignmentDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok().build();
    }
}
