package com.acharya.dikshanta.HospitalManagement.patient.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse(
        UUID id,
        String patientNumber,
        String fullName,
        String address,
        LocalDate dateOfBirth,
        String gender,
        String bloodGroup,
        String phoneNumber,
        String email,
        String emergencyContactName,
        String emergencyContactPhone,
        String emergencyContactRelation,
        String medicalHistory,
        String allergies
        ) {
}

