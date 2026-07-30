package com.acharya.dikshanta.HospitalManagement.doctor.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateSpecializationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.SpecializationResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.SpecializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specializations")
@RequiredArgsConstructor
public class SpecializationController {
    private final SpecializationService specializationService;

    @PostMapping
    public ResponseEntity<ApiResponse<SpecializationResponse>> createSpecialization(
            @Valid @RequestBody CreateSpecializationRequest request) {
        var specializationResponse = specializationService.createSpecialization(request);
        return ResponseEntity.ok(ApiResponse.success(specializationResponse, "Specialization created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecializationResponse>>> getSpecializations() {
        var response = specializationService.getSpecializations();
        return ResponseEntity.ok(ApiResponse.success(response, "Specializations fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecializationResponse>> getSpecialization(@PathVariable UUID id) {
        var response = specializationService.getSpecialization(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Specialization fetched successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSpecialization(@PathVariable UUID id) {
        specializationService.deleteSpecialization(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Specialization deleted successfully"));
    }
}
