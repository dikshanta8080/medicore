package com.acharya.dikshanta.HospitalManagement.patient.dto.response;

import java.time.LocalDate;

public record PatientResponse(
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
