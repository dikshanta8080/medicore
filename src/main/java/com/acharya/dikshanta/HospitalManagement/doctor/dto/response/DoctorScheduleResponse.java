package com.acharya.dikshanta.HospitalManagement.doctor.dto.response;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import lombok.Builder;

import java.time.LocalTime;
import java.util.UUID;

@Builder
public record DoctorScheduleResponse(
        UUID id,
        UUID doctorId,
        String doctorName,
        String department,
        String specialization,
        Days day,
        LocalTime startTime,
        LocalTime endTime
) {
}
