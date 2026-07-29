package com.acharya.dikshanta.HospitalManagement.identity.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.FilterDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/receptionist")
@RequiredArgsConstructor
public class ReceptionistController {
    private final DoctorScheduleService doctorScheduleService;

    @GetMapping("/doctors/schedules")
    public ResponseEntity<ApiResponse<PagedResponse<DoctorScheduleResponse>>> searchAvailableDoctors(
            @ModelAttribute PaginationRequest paginationRequest,
            @ModelAttribute FilterDoctorScheduleRequest filterRequest) {
        var pagedResponse = doctorScheduleService.getSchedules(filterRequest, paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Available doctors fetched successfully"));
    }

    @GetMapping("/doctors/schedules/{id}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> getDoctorSchedule(@PathVariable UUID id) {
        var scheduleResponse = doctorScheduleService.getSchedule(id);
        return ResponseEntity.ok().body(ApiResponse.success(scheduleResponse, "Doctor schedule fetched successfully"));
    }
}
