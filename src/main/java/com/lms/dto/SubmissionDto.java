package com.lms.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionDto {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private String fileName;
    private String fileUrl;
    private LocalDateTime submissionDate;

    @Min(value = 0, message = "Grade cannot be negative")
    @Max(value = 100, message = "Grade cannot exceed 100")
    private Double grade;

    @Size(max = 500, message = "Feedback cannot exceed 500 characters")
    private String feedback;

    private Boolean isLate;
}
