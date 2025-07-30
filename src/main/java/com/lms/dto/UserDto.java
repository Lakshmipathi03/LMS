package com.lms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_INSTRUCTOR|ROLE_STUDENT)$", 
             message = "Invalid role. Must be one of: ROLE_ADMIN, ROLE_INSTRUCTOR, ROLE_STUDENT")
    private String role;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
