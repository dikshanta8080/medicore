package com.acharya.dikshanta.HospitalManagement.patient.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.BloodGroup;
import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(name = "unq_patient_number", columnNames = {"patient_number"}),
        @UniqueConstraint(name = "unq_patient_phone", columnNames = {"phone_number"}),
        @UniqueConstraint(name = "unq_patient_email", columnNames = {"email"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Patient extends SoftDeleteEntity {

    @Column(name = "patient_number", nullable = false, unique = true)
    private String patientNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bloodGroup;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relation")
    private String emergencyContactRelation;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
