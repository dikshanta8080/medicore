package com.acharya.dikshanta.HospitalManagement.appointment.controller;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.CreateAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.RescheduleAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.appointment.service.AppointmentService;
import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_WRITE')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {
        var response = appointmentService.createAppointment(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Appointment created successfully"));
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointment(@PathVariable UUID appointmentId) {
        var response = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Appointment fetched successfully"));
    }

    @GetMapping("/today")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getTodayAppointments() {
        var responses = appointmentService.getTodayAppointments();
        return ResponseEntity.ok(ApiResponse.success(responses, "Today's appointments fetched successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var responses = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(ApiResponse.success(responses, "Appointments fetched successfully"));
    }

    @PatchMapping("/{appointmentId}/check-in")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> checkInAppointment(@PathVariable UUID appointmentId) {
        var response = appointmentService.checkInAppointment(appointmentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Appointment checked in successfully"));
    }

    @PatchMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(@PathVariable UUID appointmentId) {
        var response = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Appointment cancelled successfully"));
    }

    @PatchMapping("/{appointmentId}/reschedule")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> rescheduleAppointment(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody RescheduleAppointmentRequest request) {
        var response = appointmentService.rescheduleAppointment(appointmentId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Appointment rescheduled successfully"));
    }
}
