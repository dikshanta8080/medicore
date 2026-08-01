package com.acharya.dikshanta.HospitalManagement.billing.mapper;

import com.acharya.dikshanta.HospitalManagement.billing.dto.request.RecordPaymentRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.PaymentResponse;
import com.acharya.dikshanta.HospitalManagement.billing.model.Invoice;
import com.acharya.dikshanta.HospitalManagement.billing.model.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentMapper {
    public Payment toEntity(RecordPaymentRequest request, Invoice invoice){
        return Payment.builder()
                .amountPaid(request.amountPaid())
                .paymentMethod(request.paymentMethod())
                .transactionId(request.transactionId())
                .transactionDate(request.transactionDate())
                .invoice(invoice)
                .build();
    }

    public PaymentResponse toResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .invoiceId(payment.getInvoice().getId())
                .amountPaid(payment.getAmountPaid())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .transactionDate(payment.getTransactionDate())
                .build();
    }

}
