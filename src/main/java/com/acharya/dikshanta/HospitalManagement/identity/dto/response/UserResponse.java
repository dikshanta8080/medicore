package com.acharya.dikshanta.HospitalManagement.identity.dto.response;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID userId,
        String username,
        String email,
        String name,
        String phoneNumber,
        Gender gender
) {
}
