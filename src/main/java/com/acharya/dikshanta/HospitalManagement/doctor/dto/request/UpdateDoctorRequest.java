package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateDoctorRequest(
        @NotNull(message = "Doctor ID is required") UUID doctorId,
        String name,
        String address,
        String phoneNumber,
        BigDecimal consultationFee,
        UUID departmentId,
        UUID specializationId
) {
}
