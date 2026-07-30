package com.acharya.dikshanta.HospitalManagement.prescription.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.request.CreatePrescriptionRequest;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.request.UpdatePrescriptionRequest;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.response.PrescriptionResponse;
import com.acharya.dikshanta.HospitalManagement.prescription.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/consultations/{consultationId}/prescriptions")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(
            @PathVariable UUID consultationId,
            @Valid @RequestBody CreatePrescriptionRequest request) {
        var response = prescriptionService.createPrescription(consultationId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Prescription created successfully"));
    }

    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescription(@PathVariable UUID id) {
        var response = prescriptionService.getPrescription(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Prescription fetched successfully"));
    }

    @PutMapping("/prescriptions/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> updatePrescription(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePrescriptionRequest request) {
        var response = prescriptionService.updatePrescription(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Prescription updated successfully"));
    }

    @DeleteMapping("/prescriptions/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePrescription(@PathVariable UUID id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Prescription deleted successfully"));
    }
}
