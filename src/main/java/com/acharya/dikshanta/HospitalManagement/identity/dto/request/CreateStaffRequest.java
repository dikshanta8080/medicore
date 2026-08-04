package com.acharya.dikshanta.HospitalManagement.identity.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import lombok.Builder;

@Builder
public record CreateStaffRequest(
        String name,
        String email,
        String username,
        String password,
        String address,
        String phoneNumber,
        Gender gender
) {
}

