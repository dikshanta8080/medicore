package com.acharya.dikshanta.HospitalManagement.consultation.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateConsultationRequest(
        @NotBlank(message = "Symptoms are required") String symptoms,
        @NotBlank(message = "Diagnosis is required") String diagnosis,
        String clinicalNotes,
        LocalDate followUpDate
) {
}
