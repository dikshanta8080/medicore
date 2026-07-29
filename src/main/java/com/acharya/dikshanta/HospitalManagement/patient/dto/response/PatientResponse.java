package com.acharya.dikshanta.HospitalManagement.patient.dto.response;

public record PatientResponse(
        String patientNumber,
        String fullName,
        String dateOfBirth,
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
