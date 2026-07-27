package com.acharya.dikshanta.HospitalManagement.identity.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staffs", uniqueConstraints = {
        @UniqueConstraint(name = "unq_phone", columnNames = {"phone_number"})})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Staff extends SoftDeleteEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne(mappedBy = "staff")
    private Doctor doctor;
}
