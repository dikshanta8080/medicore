package com.acharya.dikshanta.HospitalManagement.doctor.controller;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/doctors")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorService doctorService;

    @GetMapping("/me/appointments/today")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyTodayAppointments() {
        var responses = doctorService.getMyTodayAppointments();
        return ResponseEntity.ok(ApiResponse.success(responses, "Today's appointments fetched successfully"));
    }
}
