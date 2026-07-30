package com.acharya.dikshanta.HospitalManagement.doctor.repository;

import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID>, JpaSpecificationExecutor<Doctor> {

    Optional<Doctor> findByStaffId(UUID staffId);
}

