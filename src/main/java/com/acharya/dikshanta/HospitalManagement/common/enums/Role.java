package com.acharya.dikshanta.HospitalManagement.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Role {

    SUPER_ADMIN(EnumSet.allOf(Permission.class)),

    ADMIN(EnumSet.of(
            Permission.DOCTOR_READ, Permission.DOCTOR_WRITE, Permission.DOCTOR_UPDATE, Permission.DOCTOR_DELETE,
            Permission.DEPARTMENT_READ, Permission.DEPARTMENT_WRITE, Permission.DEPARTMENT_UPDATE, Permission.DEPARTMENT_DELETE,
            Permission.SPECIALIZATION_READ, Permission.SPECIALIZATION_WRITE, Permission.SPECIALIZATION_UPDATE, Permission.SPECIALIZATION_DELETE,
            Permission.STAFF_READ, Permission.STAFF_WRITE, Permission.STAFF_UPDATE, Permission.STAFF_DELETE,
            Permission.PATIENT_READ, Permission.PATIENT_WRITE, Permission.PATIENT_UPDATE, Permission.PATIENT_DELETE,
            Permission.SCHEDULE_READ, Permission.SCHEDULE_WRITE, Permission.SCHEDULE_UPDATE, Permission.SCHEDULE_DELETE,
            Permission.APPOINTMENT_READ, Permission.APPOINTMENT_WRITE, Permission.APPOINTMENT_UPDATE, Permission.APPOINTMENT_DELETE,
            Permission.CONSULTATION_READ, Permission.CONSULTATION_WRITE, Permission.CONSULTATION_UPDATE, Permission.CONSULTATION_DELETE,
            Permission.PRESCRIPTION_READ, Permission.PRESCRIPTION_WRITE, Permission.PRESCRIPTION_UPDATE, Permission.PRESCRIPTION_DELETE
    )),

    RECEPTIONIST(EnumSet.of(
            Permission.DOCTOR_READ,
            Permission.DEPARTMENT_READ,
            Permission.SPECIALIZATION_READ,
            Permission.STAFF_READ,
            Permission.PATIENT_READ, Permission.PATIENT_WRITE, Permission.PATIENT_UPDATE,
            Permission.SCHEDULE_READ,
            Permission.APPOINTMENT_READ, Permission.APPOINTMENT_WRITE, Permission.APPOINTMENT_UPDATE
    )),

    DOCTOR(EnumSet.of(
            Permission.DOCTOR_READ,
            Permission.DEPARTMENT_READ,
            Permission.SPECIALIZATION_READ,
            Permission.PATIENT_READ, Permission.PATIENT_UPDATE,
            Permission.SCHEDULE_READ,
            Permission.APPOINTMENT_READ, Permission.APPOINTMENT_UPDATE,
            Permission.CONSULTATION_READ, Permission.CONSULTATION_WRITE, Permission.CONSULTATION_UPDATE,
            Permission.PRESCRIPTION_READ, Permission.PRESCRIPTION_WRITE, Permission.PRESCRIPTION_UPDATE, Permission.PRESCRIPTION_DELETE
    )),

    PATIENT(EnumSet.of(
            Permission.PATIENT_READ,
            Permission.APPOINTMENT_READ, Permission.APPOINTMENT_WRITE, Permission.APPOINTMENT_UPDATE,
            Permission.CONSULTATION_READ,
            Permission.PRESCRIPTION_READ
    ));

    private final Set<Permission> permissions;
}
