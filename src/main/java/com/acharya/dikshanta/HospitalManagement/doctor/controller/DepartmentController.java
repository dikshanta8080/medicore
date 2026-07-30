package com.acharya.dikshanta.HospitalManagement.doctor.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.AssignHodRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.RemoveHodRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.ReplaceHodRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DepartmentResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {
        var departmentResponse = departmentService.createDepartment(request);
        return ResponseEntity.ok().body(ApiResponse.success(departmentResponse, "Department created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DepartmentResponse>>> getDepartments(
            @ModelAttribute PaginationRequest paginationRequest) {
        var pagedResponse = departmentService.getDepartments(paginationRequest.toPageable());
        return ResponseEntity.ok().body(ApiResponse.success(pagedResponse, "Departments fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(@PathVariable UUID id) {
        var departmentResponse = departmentService.getDepartment(id);
        return ResponseEntity.ok().body(ApiResponse.success(departmentResponse, "Department fetched successfully"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @RequestBody @Valid UpdateDepartmentRequest request) {
        var departmentResponse = departmentService.updateDepartment(request);
        return ResponseEntity.ok().body(ApiResponse.success(departmentResponse, "Department updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Department deleted successfully"));
    }

    @PostMapping("/hod")
    public ResponseEntity<ApiResponse<DoctorResponse>> assignHod(@RequestBody @Valid AssignHodRequest request) {
        var doctorResponse = departmentService.assignHod(request);
        return ResponseEntity.ok(ApiResponse.success(doctorResponse, "HOD assigned successfully"));
    }

    @PatchMapping("/hod")
    public ResponseEntity<ApiResponse<DoctorResponse>> replaceHod(@RequestBody @Valid ReplaceHodRequest request) {
        var doctorResponse = departmentService.replaceHod(request);
        return ResponseEntity.ok(ApiResponse.success(doctorResponse, "HOD replaced successfully"));
    }

    @DeleteMapping("/hod")
    public ResponseEntity<ApiResponse<DepartmentResponse>> removeHod(@RequestBody @Valid RemoveHodRequest request) {
        var departmentResponse = departmentService.removeHod(request);
        return ResponseEntity.ok(ApiResponse.success(departmentResponse, "HOD removed successfully"));
    }
}
