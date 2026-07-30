package com.acharya.dikshanta.HospitalManagement.prescription.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PrescriptionItemResponse(
        UUID id,
        String medicineName,
        String dosage,
        String frequency,
        String duration,
        String instructions
) {
}
