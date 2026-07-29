package com.acharya.dikshanta.HospitalManagement.patient.repository;

import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPatientNumber(String patientNumber);
}