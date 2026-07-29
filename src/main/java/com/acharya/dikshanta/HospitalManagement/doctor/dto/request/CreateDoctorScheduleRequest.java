package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;

import java.time.LocalTime;
import java.util.UUID;

public record CreateDoctorScheduleRequest(
        UUID doctorId,
        Days dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
