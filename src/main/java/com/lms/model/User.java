package com.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "Please provide a valid email address")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enabled = true;

    @OneToMany(mappedBy = "instructor")
    private Set<Course> taughtCourses = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Submission> submissions = new HashSet<>();

    public enum Role {
        STUDENT, INSTRUCTOR, ADMIN
    }
}
