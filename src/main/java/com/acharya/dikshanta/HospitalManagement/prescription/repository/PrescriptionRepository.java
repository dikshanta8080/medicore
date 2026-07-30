package com.acharya.dikshanta.HospitalManagement.prescription.repository;

import com.acharya.dikshanta.HospitalManagement.prescription.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    Optional<Prescription> findByConsultationId(UUID consultationId);

    boolean existsByConsultationId(UUID consultationId);
}
