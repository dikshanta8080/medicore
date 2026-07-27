package com.acharya.dikshanta.HospitalManagement.identity.dto.response;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import lombok.Builder;

import java.util.UUID;

@Builder
public record StaffResponse(
        UUID id,
        String name,
        String email,
        String username,
        String address,
        String phoneNumber,
        Gender gender
) {
}
