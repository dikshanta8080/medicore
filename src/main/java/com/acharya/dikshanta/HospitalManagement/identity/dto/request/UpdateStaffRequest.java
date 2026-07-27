package com.acharya.dikshanta.HospitalManagement.identity.dto.request;

import java.util.UUID;

public record UpdateStaffRequest(
        UUID staffId,
        String name,
        String address,
        String phoneNumber
) {
}
