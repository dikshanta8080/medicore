package com.acharya.dikshanta.HospitalManagement.appointment.dto.response;

import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record AppointmentResponse(
        UUID id,
        UUID patientId,
        String patientName,
        UUID doctorId,
        String doctorName,
        UUID departmentId,
        String department,
        LocalTime appointmentTime,
        LocalDate appointmentDate,
        String reason,
        String allergies,
        AppointmentStatus appointmentStatus,
        String bookedBy
) {
}
