package com.acharya.dikshanta.HospitalManagement.appointment.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentRequest(
        UUID doctorId,
        UUID patientId,
        UUID departmentId,
        String reason,
        LocalDate appointmentDate,
        LocalTime appointmentTime

) {
}
