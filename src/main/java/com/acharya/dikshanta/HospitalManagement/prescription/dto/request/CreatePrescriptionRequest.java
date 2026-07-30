package com.acharya.dikshanta.HospitalManagement.prescription.dto.request;

import com.acharya.dikshanta.HospitalManagement.prescription.dto.request.PrescriptionItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreatePrescriptionRequest(
        @NotEmpty(message = "At least one prescription item is required")
        List<@Valid PrescriptionItemRequest> items
) {
}
