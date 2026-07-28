package com.acharya.dikshanta.HospitalManagement.identity.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.DoctorFilterRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DepartmentResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DepartmentService;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DoctorService;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final StaffService staffService;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;

    // Staff Endpoints
    @PostMapping("/staffs")
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        var staffResponse = staffService.createStaff(request);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff created successfully"));
    }

    @GetMapping("/staffs")
    public ResponseEntity<ApiResponse<PagedResponse<StaffResponse>>> getStaffs(@ModelAttribute PaginationRequest paginationRequest) {
        var pagedResponse = staffService.getStaffs(paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Employee fetched successfully"));
    }

    @GetMapping("/staffs/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaff(@PathVariable UUID id) {
        var staffResponse = staffService.getStaff(id);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff fetched by id"));
    }

    @PutMapping("/staffs")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(@RequestBody @Valid UpdateStaffRequest request) {
        var staffResponse = staffService.updateStaff(request);
        return ResponseEntity.ok().body(ApiResponse.success(staffResponse, "Staff updated successfully"));
    }

    @DeleteMapping("/staffs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable UUID id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Staff deleted successfully"));
    }

    // Department Endpoints
    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        var departmentResponse = departmentService.createDepartment(request);
        return ResponseEntity.ok().body(ApiResponse.success(departmentResponse, "Department created successfully"));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<PagedResponse<DepartmentResponse>>> getDepartments(@ModelAttribute PaginationRequest paginationRequest) {
        var pagedResponse = departmentService.getDepartments(paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Departments fetched successfully"));
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(@PathVariable UUID id) {
        var departmentResponse = departmentService.getDepartment(id);
        return ResponseEntity.ok().body(ApiResponse.success(departmentResponse, "Department fetched by id"));
    }

    @PutMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(@RequestBody @Valid UpdateDepartmentRequest request) {
        var departmentResponse = departmentService.updateDepartment(request);
        return ResponseEntity.ok().body(ApiResponse.success(departmentResponse, "Department updated successfully"));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Department deleted successfully"));
    }

    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@RequestBody @Valid CreateDoctorRequest request) {
        var doctorResponse = doctorService.createDoctor(request);
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponse, "Doctor created successfully"));
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor(@PathVariable UUID id) {
        var doctorResponse = doctorService.getDoctor(id);
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponse, "Doctor fetched by id"));
    }

    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<PagedResponse<DoctorResponse>>> getDoctors
            (
                    @ModelAttribute PaginationRequest request,
                    @ModelAttribute DoctorFilterRequest doctorFilterRequest) {
        var doctorResponses = doctorService.getDoctors(doctorFilterRequest, request.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponses, "doctors fetched successfully"));
    }

    @PutMapping("/doctors")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(@RequestBody @Valid UpdateDoctorRequest request) {
        var doctorResponse = doctorService.updateDoctor(request);
        return ResponseEntity.ok().body(ApiResponse.success(doctorResponse, "Doctor updated successfully"));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Doctor deleted successfully"));
    }
}
