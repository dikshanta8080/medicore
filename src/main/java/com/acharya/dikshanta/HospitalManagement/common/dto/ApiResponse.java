package com.acharya.dikshanta.HospitalManagement.common.dto;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        boolean status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status(false)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .status(false)
                .message(message)
                .data(data)
                .build();
    }
}
