package com.acharya.dikshanta.HospitalManagement.prescription.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record PrescriptionResponse(
        UUID id,
        UUID consultationId,
        List<PrescriptionItemResponse> items
) {
}
