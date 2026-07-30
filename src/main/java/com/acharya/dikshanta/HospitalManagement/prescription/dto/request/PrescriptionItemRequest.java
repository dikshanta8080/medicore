package com.acharya.dikshanta.HospitalManagement.prescription.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PrescriptionItemRequest(
        @NotBlank(message = "Medicine name is required") String medicineName,
        @NotBlank(message = "Dosage is required") String dosage,
        @NotBlank(message = "Frequency is required") String frequency,
        @NotBlank(message = "Duration is required") String duration,
        String instructions
) {
}
