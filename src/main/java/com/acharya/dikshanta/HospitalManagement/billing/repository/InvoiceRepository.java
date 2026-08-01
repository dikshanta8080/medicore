package com.acharya.dikshanta.HospitalManagement.billing.repository;

import com.acharya.dikshanta.HospitalManagement.billing.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // FIXED: Spring Data Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Page<Invoice> findByPatientId(UUID patientId, Pageable pageable);

    boolean existsByAppointmentId(UUID appointmentId);
}