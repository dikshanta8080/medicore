package com.acharya.dikshanta.HospitalManagement.doctor.model;

import com.acharya.dikshanta.HospitalManagement.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors = new ArrayList<>();
}
