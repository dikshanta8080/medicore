package com.acharya.dikshanta.HospitalManagement.billing.model;

import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "invoices", uniqueConstraints = {
        @UniqueConstraint(name = "unq_invoice_number", columnNames = {"invoice_number"})
})
@SQLDelete(sql = "UPDATE invoices SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Invoice extends SoftDeleteEntity {

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "tax_amount", nullable = false)
    private BigDecimal taxAmount;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "balance_due", nullable = false)
    private BigDecimal balanceDue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoices;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


}
