package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.UUID;

public record FilterDoctorScheduleRequest(
        Days day,
        UUID doctorId,
        UUID departmentId,
        UUID specializationId,
        String doctorName,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        LocalTime availableAt
) {
}
