package com.acharya.dikshanta.HospitalManagement.patient.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.BloodGroup;
import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Entity
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(name = "unq_patient_number", columnNames = {"patient_number"}),
        @UniqueConstraint(name = "unq_patient_phone", columnNames = {"phone_number"}),
        @UniqueConstraint(name = "unq_patient_email", columnNames = {"email"})
})
@SQLDelete(sql = "UPDATE patients SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Patient extends SoftDeleteEntity {

    @Column(name = "patient_number", nullable = false, unique = true)
    private String patientNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false)
    private BloodGroup bloodGroup;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "emergency_contact_name",  nullable = false)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", nullable = false)
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relation", nullable = false)
    private String emergencyContactRelation;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;


}
