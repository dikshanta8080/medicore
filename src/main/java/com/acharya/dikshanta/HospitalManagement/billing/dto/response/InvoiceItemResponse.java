package com.acharya.dikshanta.HospitalManagement.billing.dto.response;

import com.acharya.dikshanta.HospitalManagement.billing.model.ItemType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record InvoiceItemResponse(
        UUID id,
        String description,
        BigDecimal unitPrice,
        Long quantity,
        BigDecimal lineTotal,
        ItemType itemType
) {}