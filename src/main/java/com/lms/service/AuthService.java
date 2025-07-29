package com.lms.service;

import com.lms.dto.LoginRequest;
import com.lms.dto.LoginResponse;
import com.lms.model.User;

public interface AuthService {
    LoginResponse authenticate(LoginRequest loginRequest);
    boolean validateToken(String token);
    User getCurrentUser();
}
