package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentRequest(
        @NotBlank(message = "Department name is required") String name,
        @NotBlank(message = "Description is required") String description
) {
}
