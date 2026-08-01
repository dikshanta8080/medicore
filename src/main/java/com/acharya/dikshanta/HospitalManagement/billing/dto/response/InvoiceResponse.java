package com.acharya.dikshanta.HospitalManagement.billing.dto.response;

import com.acharya.dikshanta.HospitalManagement.billing.model.Status;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record InvoiceResponse(
        UUID id,
        String invoiceNumber,
        UUID patientId,
        BigDecimal subTotal,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal amountPaid,
        BigDecimal balanceDue,
        BigDecimal totalAmount,
        Status status,
        List<InvoiceItemResponse> items,
        LocalDateTime createdAt
) {}