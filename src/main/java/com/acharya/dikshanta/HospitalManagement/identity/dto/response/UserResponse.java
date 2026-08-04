package com.acharya.dikshanta.HospitalManagement.identity.dto.response;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID userId,
        String username,
        String email,
        Role role,
        String name,
        String phoneNumber,
        Gender gender
) {
}
