package com.acharya.dikshanta.HospitalManagement.consultation.model;

import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import com.acharya.dikshanta.HospitalManagement.prescription.model.Prescription;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Consultation extends SoftDeleteEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(nullable = false)
    private String symptoms;

    @Column(nullable = false)
    private String diagnosis;

    @Column(length = 2000)
    private String clinicalNotes;

    private LocalDate followUpDate;

    @OneToOne(mappedBy = "consultation", fetch = FetchType.LAZY)
    private Prescription prescription;
}
