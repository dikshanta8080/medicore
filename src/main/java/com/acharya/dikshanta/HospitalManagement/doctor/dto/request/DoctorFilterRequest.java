package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;

import java.util.UUID;

public record DoctorFilterRequest(
        String name,
        Gender gender,
        UUID departmentId,
        UUID specializationId
) {
}
