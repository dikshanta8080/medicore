package com.acharya.dikshanta.HospitalManagement.doctor.model;

import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specialization")
@SQLDelete(sql = "UPDATE specialization SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Specialization extends SoftDeleteEntity {
    @Column(name = "name", unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors = new ArrayList<>();
}
