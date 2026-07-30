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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "invoice_items")
@SQLDelete(sql = "UPDATE invoice_items SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")

public class InvoiceItem extends SoftDeleteEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "line_total", nullable = false)
    private BigDecimal lineTotal;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}
