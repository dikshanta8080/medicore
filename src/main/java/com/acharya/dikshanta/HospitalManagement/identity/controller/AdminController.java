package com.acharya.dikshanta.HospitalManagement.identity.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.CreateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.UpdateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.StaffResponse;
import com.acharya.dikshanta.HospitalManagement.identity.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/staffs")
@RequiredArgsConstructor
public class AdminController {
    private final StaffService staffService;

    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        var staffResponse = staffService.createStaff(request);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<StaffResponse>>> getStaffs(@ModelAttribute PaginationRequest paginationRequest) {
        var pagedResponse = staffService.getStaffs(paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Employee fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaff(@PathVariable UUID id) {
        var staffResponse = staffService.getStaff(id);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff fetched by id"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(@RequestBody @Valid UpdateStaffRequest request) {
        var staffResponse = staffService.updateStaff(request);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable UUID id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Staff deleted successfully"));
    }
}
