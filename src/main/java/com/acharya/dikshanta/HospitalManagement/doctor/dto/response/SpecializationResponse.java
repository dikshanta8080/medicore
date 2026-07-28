package com.acharya.dikshanta.HospitalManagement.doctor.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SpecializationResponse(
        UUID id,
        String name
) {
}
