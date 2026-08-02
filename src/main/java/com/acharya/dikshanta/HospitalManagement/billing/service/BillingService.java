package com.acharya.dikshanta.HospitalManagement.billing.service;

import com.acharya.dikshanta.HospitalManagement.appointment.event.AppointmentBookedEvent;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import com.acharya.dikshanta.HospitalManagement.billing.dto.request.AddInvoiceItemRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.request.CreateInvoiceRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.request.RecordPaymentRequest;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.InvoiceResponse;
import com.acharya.dikshanta.HospitalManagement.billing.dto.response.PaymentResponse;
import com.acharya.dikshanta.HospitalManagement.billing.mapper.InvoiceItemMapper;
import com.acharya.dikshanta.HospitalManagement.billing.mapper.InvoiceMapper;
import com.acharya.dikshanta.HospitalManagement.billing.mapper.PaymentMapper;
import com.acharya.dikshanta.HospitalManagement.billing.model.Invoice;
import com.acharya.dikshanta.HospitalManagement.billing.model.InvoiceItem;
import com.acharya.dikshanta.HospitalManagement.billing.model.ItemType;
import com.acharya.dikshanta.HospitalManagement.billing.model.Payment;
import com.acharya.dikshanta.HospitalManagement.billing.model.Status;
import com.acharya.dikshanta.HospitalManagement.billing.repository.InvoiceRepository;
import com.acharya.dikshanta.HospitalManagement.billing.repository.PaymentRepository;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.acharya.dikshanta.HospitalManagement.patient.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;
    private final InvoiceMapper invoiceMapper;
    private final PaymentMapper paymentMapper;
    private final InvoiceItemMapper invoiceItemMapper;
    private final PdfGeneratorService pdfGeneratorService;

    @PersistenceContext
    private final EntityManager entityManager;

    // 1. MANUAL INVOICE CREATION
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + request.patientId()));

        BigDecimal subTotal = BigDecimal.ZERO;
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (AddInvoiceItemRequest itemReq : request.items()) {
            InvoiceItem item = invoiceItemMapper.toEntity(itemReq);
            subTotal = subTotal.add(item.getLineTotal());
            invoiceItems.add(item);
        }

        BigDecimal discountAmount = request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO;
        BigDecimal taxPercentage = request.taxPercentage() != null ? request.taxPercentage() : BigDecimal.ZERO;

        BigDecimal taxableAmount = subTotal.subtract(discountAmount);
        if (taxableAmount.compareTo(BigDecimal.ZERO) < 0) {
            taxableAmount = BigDecimal.ZERO;
        }

        BigDecimal taxAmount = taxableAmount.multiply(taxPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = taxableAmount.add(taxAmount);

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .patient(patient)
                .subTotal(subTotal)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .amountPaid(BigDecimal.ZERO)
                .balanceDue(totalAmount)
                .invoiceStatus(Status.PENDING)
                .invoiceItems(new ArrayList<>())
                .build();

        for (InvoiceItem item : invoiceItems) {
            item.setInvoice(invoice);
            invoice.getInvoiceItems().add(item);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }

    // 2. AUTOMATIC INVOICE GENERATION
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createInvoiceFromAppointment(AppointmentBookedEvent event) {

        Appointment appointment = entityManager.find(Appointment.class, event.appointmentId());
        if(appointment == null){
            throw new ResourceNotFoundException("appointment is null");
        }

        if (invoiceRepository.existsByAppointmentId(event.appointmentId())) {
            return;
        }

        BigDecimal consultationFee = (appointment.getDoctor() != null && appointment.getDoctor().getConsultationFee() != null)
                ? appointment.getDoctor().getConsultationFee()
                : BigDecimal.valueOf(500.00);

        BigDecimal subTotal = consultationFee;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = subTotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = subTotal.add(taxAmount).subtract(discountAmount);

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .patient(appointment.getPatient())
                .appointment(appointment)
                .subTotal(subTotal)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .amountPaid(BigDecimal.ZERO)
                .balanceDue(totalAmount)
                .invoiceStatus(Status.PENDING)
                .invoiceItems(new ArrayList<>())
                .build();

        InvoiceItem consultationItem = InvoiceItem.builder()
                .description("Consultation Fee - " + (appointment.getDoctor() != null ? appointment.getDoctor().getLicenseNumber() : "General"))
                .unitPrice(consultationFee)
                .quantity(1L)
                .lineTotal(consultationFee)
                    .itemType(ItemType.DOCTOR_FEE)
                .invoice(invoice)
                .build();

        invoice.getInvoiceItems().add(consultationItem);
        invoiceRepository.save(invoice);
    }

    // 3. READ OPERATIONS
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getInvoicesByPatient(UUID patientId, Pageable pageable) {
        return invoiceRepository.findByPatientId(patientId, pageable)
                .map(invoiceMapper::toResponse);
    }

    // 4. RECORD PAYMENT & UPDATE BALANCES / STATUS
    @Transactional
    public PaymentResponse recordPayment(UUID invoiceId, RecordPaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + invoiceId));

        if (invoice.getInvoiceStatus() == Status.PAID) {
            throw new IllegalStateException("Invoice is already fully paid.");
        }

        if (request.amountPaid().compareTo(invoice.getBalanceDue()) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds remaining balance due: " + invoice.getBalanceDue());
        }

        Payment payment = paymentMapper.toEntity(request, invoice);
        Payment savedPayment = paymentRepository.save(payment);

        BigDecimal newAmountPaid = invoice.getAmountPaid().add(request.amountPaid());
        BigDecimal newBalanceDue = invoice.getTotalAmount().subtract(newAmountPaid);

        invoice.setAmountPaid(newAmountPaid);
        invoice.setBalanceDue(newBalanceDue);

        if (newBalanceDue.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setInvoiceStatus(Status.PAID);
        } else {
            invoice.setInvoiceStatus(Status.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);
        return paymentMapper.toResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public byte[] generatePaymentReceiptPdf(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));

        Map<String, Object> variables = Map.of(
                "receiptNumber", "REC-" + payment.getId().toString().substring(0, 8).toUpperCase(),
                "invoiceNumber", payment.getInvoice().getInvoiceNumber(),
                "patientName", payment.getInvoice().getPatient().getFullName(),
                "paymentDate", payment.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                "paymentMethod", payment.getPaymentMethod().name(),
                "transactionId", payment.getTransactionId() != null ? payment.getTransactionId() : "N/A",
                "amountPaid", payment.getAmountPaid(),
                "remainingBalance", payment.getInvoice().getBalanceDue()
        );

        return pdfGeneratorService.generatePdfFromTemplate("receipt", variables);
    }

    private Invoice buildInvoice(Appointment appointment, BigDecimal subTotal, BigDecimal discountAmount, BigDecimal taxAmount, BigDecimal totalAmount) {
        return Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .patient(appointment.getPatient())
                .appointment(appointment)
                .subTotal(subTotal)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .amountPaid(BigDecimal.ZERO)
                .balanceDue(totalAmount)
                .invoiceStatus(Status.PENDING)
                .invoiceItems(new ArrayList<>())
                .build();
    }

    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}