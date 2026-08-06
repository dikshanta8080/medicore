package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record CreateDoctorScheduleRequest(
        @NotNull(message = "Doctor ID is required")
        UUID doctorId,

        @NotNull(message = "Day of week is required")
        Days dayOfWeek,

        @NotNull(message = "Start time is required")
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime endTime
) {
}
