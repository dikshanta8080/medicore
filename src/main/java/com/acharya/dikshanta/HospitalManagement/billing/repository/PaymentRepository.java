package com.acharya.dikshanta.HospitalManagement.billing.repository;

import com.acharya.dikshanta.HospitalManagement.billing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByInvoiceId(UUID invoiceId);

    Optional<Payment> findByTransactionId(String transactionId);
}