package com.lms.service.impl;

import com.lms.dto.LoginRequest;
import com.lms.dto.LoginResponse;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.security.JwtTokenProvider;
import com.lms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = tokenProvider.generateToken(user.getUsername(), user.getRole().name());

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());

        return response;
    }

    @Override
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
