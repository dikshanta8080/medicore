package com.acharya.dikshanta.HospitalManagement.common.model;

import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Specialization;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoftDeleteEntityTest {

    @Test
    @DisplayName("Should default deleted to false and perform soft delete correctly")
    void testPerformSoftDelete() {
        User user = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .build();

        assertFalse(user.isDeleted(), "Entity should default to deleted = false");
        assertNull(user.getDeletedAt(), "deletedAt should initially be null");

        user.performSoftDelete();

        assertTrue(user.isDeleted(), "Entity should have deleted = true after performSoftDelete()");
        assertNotNull(user.getDeletedAt(), "deletedAt timestamp should be set after performSoftDelete()");
    }

    @Test
    @DisplayName("Verify all domain entities inherit SoftDeleteEntity")
    void testEntityInheritance() {
        assertTrue(SoftDeleteEntity.class.isAssignableFrom(User.class));
        assertTrue(SoftDeleteEntity.class.isAssignableFrom(Staff.class));
        assertTrue(SoftDeleteEntity.class.isAssignableFrom(Patient.class));
        assertTrue(SoftDeleteEntity.class.isAssignableFrom(Doctor.class));
        assertTrue(SoftDeleteEntity.class.isAssignableFrom(Department.class));
        assertTrue(SoftDeleteEntity.class.isAssignableFrom(Specialization.class));
    }
}
