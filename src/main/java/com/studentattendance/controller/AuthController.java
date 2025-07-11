package com.studentattendance.controller;

import com.studentattendance.dtos.*;
import com.studentattendance.errorhandler.ServiceResult;
import com.studentattendance.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Authentication", description = "Authentication Api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        ServiceResult<RegisterResponse> result = authService.register(request);
        return ResponseEntity.status(result.getData().getCode()).body(result.getData());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        ServiceResult<LoginResponse> result = authService.login(request);
        return ResponseEntity.status(result.getData().getCode()).body(result.getData());
    }
}