package com.lms.service;

import com.lms.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User updateUser(User user);
    void deleteUser(Long userId);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllUsers();
    List<User> findAllInstructors();
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    boolean updateRole(Long userId, User.Role newRole);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void enableUser(Long userId);
    void disableUser(Long userId);
}
