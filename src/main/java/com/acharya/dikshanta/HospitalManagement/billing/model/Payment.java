package com.acharya.dikshanta.HospitalManagement.billing.model;

import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(name = "unq_transaction_id", columnNames = "transaction_id")
})
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE payments SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Payment extends SoftDeleteEntity {

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}
