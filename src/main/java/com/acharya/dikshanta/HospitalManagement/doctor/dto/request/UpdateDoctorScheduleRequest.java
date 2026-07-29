package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record UpdateDoctorScheduleRequest(
        @NotNull(message = "Schedule ID is required")
        UUID scheduleId,

        Days dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
