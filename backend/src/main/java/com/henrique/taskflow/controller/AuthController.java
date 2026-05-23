package com.henrique.taskflow.controller;

import com.henrique.taskflow.dto.request.LoginRequest;
import com.henrique.taskflow.dto.request.RegisterRequest;
import com.henrique.taskflow.dto.response.LoginResponse;
import com.henrique.taskflow.dto.response.UserResponse;
import com.henrique.taskflow.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register (@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse response = authService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest loginRequest) {
        var response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}
