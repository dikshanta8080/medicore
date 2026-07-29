package com.acharya.dikshanta.HospitalManagement.appointment.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AppointmentResponse(
        String patientName,
        String doctorName,
        LocalTime appointmentTime,
        LocalDate appointmentDate,
        String department,
        String reason,
        String allergies,
        String bookedBy


) {
}
