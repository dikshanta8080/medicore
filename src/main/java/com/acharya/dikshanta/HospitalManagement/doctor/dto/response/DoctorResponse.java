package com.acharya.dikshanta.HospitalManagement.doctor.dto.response;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DoctorResponse(
        String name,
        String email,
        String username,
        String address,
        String phoneNumber,
        Gender gender,
        BigDecimal consultationFee,
        String licenseNumber,
        String specialization,
        String department
) {
}
