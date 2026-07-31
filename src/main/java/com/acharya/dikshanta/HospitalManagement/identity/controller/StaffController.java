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
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasAuthority('STAFF_WRITE')")
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        var staffResponse = staffService.createStaff(request);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STAFF_READ')")
    public ResponseEntity<ApiResponse<PagedResponse<StaffResponse>>> getStaffs(
            @ModelAttribute PaginationRequest paginationRequest) {
        var pagedResponse = staffService.getStaffs(paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Staff fetched successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF_READ')")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaff(@PathVariable UUID id) {
        var staffResponse = staffService.getStaff(id);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff fetched successfully"));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('STAFF_UPDATE')")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(@RequestBody @Valid UpdateStaffRequest request) {
        var staffResponse = staffService.updateStaff(request);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable UUID id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Staff deleted successfully"));
    }
}
