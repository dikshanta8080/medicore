package com.acharya.dikshanta.HospitalManagement.doctor.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DepartmentResponse(
        UUID id,
        String name,
        String description
) {
}
