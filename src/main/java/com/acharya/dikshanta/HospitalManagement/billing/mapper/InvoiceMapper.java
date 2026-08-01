package com.acharya.dikshanta.HospitalManagement.billing.mapper;

import com.acharya.dikshanta.HospitalManagement.billing.dto.response.InvoiceItemResponse;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.InvoiceResponse;
import com.acharya.dikshanta.HospitalManagement.billing.model.Invoice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {

    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceResponse toResponse(Invoice invoice){

        List<InvoiceItemResponse> itemResponses = invoice.getInvoiceItems() != null
                ? invoice.getInvoiceItems().stream().map(invoiceItemMapper::toResponse).toList()
                : Collections.emptyList();

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .patientId(invoice.getPatient().getId())
                .subTotal(invoice.getSubTotal())
                .discountAmount(invoice.getDiscountAmount())
                .taxAmount(invoice.getTaxAmount())
                .amountPaid(invoice.getAmountPaid())
                .balanceDue(invoice.getBalanceDue())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getInvoiceStatus())
                .items(itemResponses)
                .createdAt(invoice.getCreatedAt())
                .build();
    }
}
