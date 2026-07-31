package com.acharya.dikshanta.HospitalManagement.consultation.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.request.CreateConsultationRequest;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.request.UpdateConsultationRequest;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.response.ConsultationResponse;
import com.acharya.dikshanta.HospitalManagement.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/appointments/{appointmentId}/consultation")
    @PreAuthorize("hasAuthority('CONSULTATION_WRITE')")
    public ResponseEntity<ApiResponse<ConsultationResponse>> startConsultation(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody CreateConsultationRequest request) {
        var response = consultationService.startConsultation(appointmentId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Consultation started successfully"));
    }

    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasAuthority('CONSULTATION_READ')")
    public ResponseEntity<ApiResponse<ConsultationResponse>> getConsultation(@PathVariable UUID id) {
        var response = consultationService.getConsultation(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Consultation fetched successfully"));
    }

    @PutMapping("/consultations/{id}")
    @PreAuthorize("hasAuthority('CONSULTATION_UPDATE')")
    public ResponseEntity<ApiResponse<ConsultationResponse>> updateConsultation(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateConsultationRequest request) {
        var response = consultationService.updateConsultation(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Consultation updated successfully"));
    }

    @PatchMapping("/consultations/{id}/complete")
    @PreAuthorize("hasAuthority('CONSULTATION_UPDATE')")
    public ResponseEntity<ApiResponse<ConsultationResponse>> completeConsultation(@PathVariable UUID id) {
        var response = consultationService.completeConsultation(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Consultation completed successfully"));
    }
}
