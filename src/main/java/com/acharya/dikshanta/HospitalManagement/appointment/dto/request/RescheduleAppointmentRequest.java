package com.acharya.dikshanta.HospitalManagement.appointment.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleAppointmentRequest(
        @NotNull(message = "Appointment date is required") LocalDate appointmentDate,
        @NotNull(message = "Appointment time is required") LocalTime appointmentTime
) {
}
