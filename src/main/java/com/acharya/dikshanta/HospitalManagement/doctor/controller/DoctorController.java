package com.acharya.dikshanta.HospitalManagement.doctor.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.DoctorFilterRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR_WRITE')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@RequestBody @Valid CreateDoctorRequest request) {
        var doctorResponse = doctorService.createDoctor(request);
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponse, "Doctor created successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor(@PathVariable UUID id) {
        var doctorResponse = doctorService.getDoctor(id);
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponse, "Doctor fetched successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    public ResponseEntity<ApiResponse<PagedResponse<DoctorResponse>>> getDoctors(
            @ModelAttribute PaginationRequest request,
            @ModelAttribute DoctorFilterRequest doctorFilterRequest) {
        var doctorResponses = doctorService.getDoctors(doctorFilterRequest, request.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponses, "Doctors fetched successfully"));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(@RequestBody @Valid UpdateDoctorRequest request) {
        var doctorResponse = doctorService.updateDoctor(request);
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponse, "Doctor updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Doctor deleted successfully"));
    }
}
