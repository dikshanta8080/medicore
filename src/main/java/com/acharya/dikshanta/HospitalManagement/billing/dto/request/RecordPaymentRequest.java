package com.acharya.dikshanta.HospitalManagement.billing.dto.request;

import com.acharya.dikshanta.HospitalManagement.billing.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RecordPaymentRequest(

        @NotNull(message = "Invoice ID is required")
        UUID invoiceId,

        @NotNull(message = "Paid amount is required")
        @Positive(message = "Amount paid must be greater than zero")
        BigDecimal amountPaid,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        String transactionId,

        @NotNull(message = "Transaction date is required")
        @PastOrPresent(message = "Transaction date cannot be in the future")
        LocalDateTime transactionDate
) {}