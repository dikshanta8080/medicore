package com.acharya.dikshanta.HospitalManagement.identity.repository;

import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
}
