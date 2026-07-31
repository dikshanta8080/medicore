package com.acharya.dikshanta.HospitalManagement.billing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record CreateInvoiceRequest(

        @NotNull(message = "Patient ID is required")
        Long patientId,

        Long appointmentId,

        @NotEmpty(message = "Invoice must contain at least one item")
        @Valid
        List<AddInvoiceItemRequest> items,

        @PositiveOrZero(message = "Discount cannot be negative")
        BigDecimal discountAmount,

        @PositiveOrZero(message = "Tax percentage cannot be negative")
        BigDecimal taxPercentage
) {}