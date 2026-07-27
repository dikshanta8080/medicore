package com.acharya.dikshanta.HospitalManagement.doctor.model;

import com.acharya.dikshanta.HospitalManagement.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "specialization")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Specialization extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;
}
