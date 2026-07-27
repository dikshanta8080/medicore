package com.acharya.dikshanta.HospitalManagement.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;

@Builder
public record LoginResponse(
        @JsonUnwrapped UserResponse userResponse,
        String token
) {
}
