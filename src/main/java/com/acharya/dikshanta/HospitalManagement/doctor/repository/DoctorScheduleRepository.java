package com.acharya.dikshanta.HospitalManagement.doctor.repository;

import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {
}
