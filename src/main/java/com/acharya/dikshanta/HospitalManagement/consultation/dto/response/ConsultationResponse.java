package com.acharya.dikshanta.HospitalManagement.consultation.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ConsultationResponse(
        UUID id,
        UUID appointmentId,
        String symptoms,
        String diagnosis,
        String clinicalNotes,
        LocalDate followUpDate
) {
}
