package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import java.util.UUID;

public record AssignHodRequest(
        UUID doctorId,
        UUID departmentId
) {
}
