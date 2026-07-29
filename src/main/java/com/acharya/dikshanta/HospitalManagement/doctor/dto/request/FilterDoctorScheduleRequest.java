package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;

import java.util.UUID;

public record FilterDoctorScheduleRequest(
        Days day,
        UUID doctorId

) {
}
