package com.acharya.dikshanta.HospitalManagement.appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentRequest(
        @NotNull(message = "Doctor ID is required") UUID doctorId,
        @NotNull(message = "Patient ID is required") UUID patientId,
        @NotNull(message = "Department ID is required") UUID departmentId,
        @NotBlank(message = "Reason is required") String reason,
        @NotNull(message = "Appointment date is required") LocalDate appointmentDate,
        @NotNull(message = "Appointment time is required") LocalTime appointmentTime
) {
}
