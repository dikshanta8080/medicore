package com.acharya.dikshanta.HospitalManagement.billing.dto.response;

import com.acharya.dikshanta.HospitalManagement.billing.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID invoiceId,
        BigDecimal amountPaid,
        PaymentMethod paymentMethod,
        String transactionId,
        LocalDateTime transactionDate
) {}