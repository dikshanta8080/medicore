package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDoctorRequest(
        String name,
        String email,
        String username,
        String password,
        String address,
        String phoneNumber,
        Gender gender,
        BigDecimal consultationFee,
        String licenseNumber,
        UUID departmentId,
        UUID specializationId


) {
}
