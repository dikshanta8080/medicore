package com.acharya.dikshanta.HospitalManagement.patient.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends SoftDeleteEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "address")
    private String address;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_number")
    private String emergencyContactNumber;
}
