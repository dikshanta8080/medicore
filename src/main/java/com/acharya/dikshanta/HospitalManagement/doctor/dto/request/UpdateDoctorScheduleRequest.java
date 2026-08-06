package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record UpdateDoctorScheduleRequest(
        @NotNull(message = "Schedule ID is required")
        UUID scheduleId,

        Days dayOfWeek,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime startTime,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime endTime
) {
}
