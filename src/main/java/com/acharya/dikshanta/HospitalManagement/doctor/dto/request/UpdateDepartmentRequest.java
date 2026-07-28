package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateDepartmentRequest(
        @NotNull(message = "Department ID is required") UUID departmentId,
        String name,
        String description
) {
}
