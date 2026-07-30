package com.acharya.dikshanta.HospitalManagement.consultation.dto.request;

import java.time.LocalDate;

public record UpdateConsultationRequest(
        String symptoms,
        String diagnosis,
        String clinicalNotes,
        LocalDate followUpDate
) {
}
