package com.acharya.dikshanta.HospitalManagement.doctor.dto.request;

import java.util.UUID;

public record ReplaceHodRequest(
        UUID departmentId,
        UUID doctorId
) {
}
