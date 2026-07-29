package com.acharya.dikshanta.HospitalManagement.patient.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record CreatePatientRequest(
        @NotBlank(message = "patient number is required")
        String patientNumber,

        @NotBlank(message = "full name is required")
        String fullName,

        @NotBlank(message = "date of birth is required")
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        LocalDate dateOfBirth,

        String gender,

        String bloodGroup,

        @NotBlank(message = "phone number is required")
        String phoneNumber,

        @Email
        @NotBlank(message = "email is required")
        String email,

        @NotBlank(message = "emergency contact number is required")
        String emergencyContactName,

        @NotBlank(message = "emergency contact phone is required")
        String emergencyContactPhone,

        @NotBlank(message = "emergency contact relation is required")
        String emergencyContactRelation,

        String medicalHistory,

        String allergies
) {
}
