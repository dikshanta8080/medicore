package com.acharya.dikshanta.HospitalManagement.identity.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateSpecializationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.SpecializationResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.SpecializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {
    private final SpecializationService specializationService;

    @PostMapping
    public ResponseEntity<ApiResponse<SpecializationResponse>> createSpecialization(@Valid @RequestBody CreateSpecializationRequest request) {
        var specializationResponse = specializationService.createSpecialization(request);
        return ResponseEntity.ok(ApiResponse.success(specializationResponse, "Specialization created"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecializationResponse>>> getSpecializations() {
        var response = specializationService.getSpecializations();
        return ResponseEntity.ok().body(ApiResponse.success(response, "Specializations fetched"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecializationResponse>> getSpecialization(@PathVariable UUID id) {
        var response = specializationService.getSpecialization(id);
        return ResponseEntity.ok().body(ApiResponse.success(response, "Specialization fetched"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSpecialization(@PathVariable UUID id) {
        specializationService.deleteSpecialization(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Specialization deleted successfully"));
    }
}
