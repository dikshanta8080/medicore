package com.acharya.dikshanta.HospitalManagement.billing.dto.request;

import com.acharya.dikshanta.HospitalManagement.billing.model.ItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddInvoiceItemRequest(

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Unit price is required")
        @Positive(message = "Unit price must be greater than zero")
        BigDecimal unitPrice,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Long quantity,

        @NotNull(message = "Item type is required")
        ItemType itemType
) {}