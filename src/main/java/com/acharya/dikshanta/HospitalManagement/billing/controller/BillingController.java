package com.acharya.dikshanta.HospitalManagement.billing.controller;

import com.acharya.dikshanta.HospitalManagement.billing.dto.request.CreateInvoiceRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.request.RecordPaymentRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.InvoiceResponse;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.PaymentResponse;
import com.acharya.dikshanta.HospitalManagement.billing.service.BillingService;
import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class BillingController {

    private final BillingService billingService;

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceResponse invoiceResponse = billingService.createInvoice(request);
        ApiResponse<InvoiceResponse> apiResponse = ApiResponse.<InvoiceResponse>builder()
                .data(invoiceResponse)
                .message("invoice created successfully")
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable UUID id) {
        InvoiceResponse invoiceResponse = billingService.getInvoiceById(id);
        ApiResponse<InvoiceResponse> apiResponse = ApiResponse.<InvoiceResponse>builder()
                .data(invoiceResponse)
                .message("invoice retrieved successfully")
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<Page<InvoiceResponse>>> getInvoicesByPatient(
            @PathVariable UUID patientId,
            @Valid PaginationRequest paginationRequest) {
        Page<InvoiceResponse> invoiceResponses = billingService.getInvoicesByPatient(patientId, paginationRequest.toPageable());
        ApiResponse<Page<InvoiceResponse>> apiResponse = ApiResponse.<Page<InvoiceResponse>>builder()
                .data(invoiceResponses)
                .message("patient invoices retrieved successfully")
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<ApiResponse<PaymentResponse>> recordPayment(
            @PathVariable UUID invoiceId,
            @Valid @RequestBody RecordPaymentRequest request) {
        PaymentResponse paymentResponse = billingService.recordPayment(invoiceId, request);
        ApiResponse<PaymentResponse> apiResponse = ApiResponse.<PaymentResponse>builder()
                .data(paymentResponse)
                .message("payment recorded successfully")
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}