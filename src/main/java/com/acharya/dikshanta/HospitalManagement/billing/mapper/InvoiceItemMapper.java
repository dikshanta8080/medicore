package com.acharya.dikshanta.HospitalManagement.billing.mapper;

import com.acharya.dikshanta.HospitalManagement.billing.dto.request.AddInvoiceItemRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.InvoiceItemResponse;
import com.acharya.dikshanta.HospitalManagement.billing.model.InvoiceItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InvoiceItemMapper {
    public InvoiceItem toEntity(AddInvoiceItemRequest request){
        BigDecimal lineTotal = request.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));
        return InvoiceItem.builder()
                .description(request.description())
                .itemType(request.itemType())
                .quantity(request.quantity())
                .unitPrice(request.unitPrice())
                .lineTotal(lineTotal)
                .build();
    }

    public InvoiceItemResponse toResponse(InvoiceItem invoiceItem){
        return InvoiceItemResponse.builder()
                .id(invoiceItem.getId())
                .description(invoiceItem.getDescription())
                .unitPrice(invoiceItem.getUnitPrice())
                .quantity(invoiceItem.getQuantity())
                .lineTotal(invoiceItem.getLineTotal())
                .itemType(invoiceItem.getItemType())
                .build();

    }
}
