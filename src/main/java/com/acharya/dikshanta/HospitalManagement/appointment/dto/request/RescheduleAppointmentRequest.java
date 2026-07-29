package com.acharya.dikshanta.HospitalManagement.appointment.dto.request;

import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RescheduleAppointmentRequest(
        @NotNull UUID appointmentId,
        @NotNull AppointmentStatus appointmentStatus
) {
}
