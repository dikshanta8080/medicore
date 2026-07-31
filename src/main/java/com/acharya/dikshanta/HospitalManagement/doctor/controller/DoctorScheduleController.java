package com.acharya.dikshanta.HospitalManagement.doctor.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.FilterDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DoctorScheduleService;
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
@RequestMapping("/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCHEDULE_WRITE')")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> createSchedule(
            @RequestBody @Valid CreateDoctorScheduleRequest request) {
        var scheduleResponse = doctorScheduleService.createSchedule(request);
        return ResponseEntity.ok().body(ApiResponse.success(scheduleResponse, "Doctor schedule created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCHEDULE_READ')")
    public ResponseEntity<ApiResponse<PagedResponse<DoctorScheduleResponse>>> getSchedules(
            @ModelAttribute PaginationRequest paginationRequest,
            @ModelAttribute FilterDoctorScheduleRequest filterRequest) {
        var pagedResponse = doctorScheduleService.getSchedules(filterRequest, paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Doctor schedules fetched successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCHEDULE_READ')")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> getSchedule(@PathVariable UUID id) {
        var scheduleResponse = doctorScheduleService.getSchedule(id);
        return ResponseEntity.ok().body(ApiResponse.success(scheduleResponse, "Doctor schedule fetched successfully"));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('SCHEDULE_UPDATE')")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> updateSchedule(
            @RequestBody @Valid UpdateDoctorScheduleRequest request) {
        var scheduleResponse = doctorScheduleService.updateSchedule(request);
        return ResponseEntity.ok().body(ApiResponse.success(scheduleResponse, "Doctor schedule updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCHEDULE_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable UUID id) {
        doctorScheduleService.deleteSchedule(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Doctor schedule deleted successfully"));
    }
}
