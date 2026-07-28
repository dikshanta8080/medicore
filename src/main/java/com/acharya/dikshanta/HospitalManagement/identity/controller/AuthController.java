package com.acharya.dikshanta.HospitalManagement.identity.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.LoginRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.LoginResponse;
import com.acharya.dikshanta.HospitalManagement.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.loginUser(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, "Login successful"));
    }
}
