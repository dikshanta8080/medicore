package com.acharya.dikshanta.HospitalManagement.common.enums;

public enum Permission {

    // ── Doctor ──────────────────────────────
    DOCTOR_READ,
    DOCTOR_WRITE,
    DOCTOR_UPDATE,
    DOCTOR_DELETE,

    // ── Department ──────────────────────────
    DEPARTMENT_READ,
    DEPARTMENT_WRITE,
    DEPARTMENT_UPDATE,
    DEPARTMENT_DELETE,

    // ── Specialization ──────────────────────
    SPECIALIZATION_READ,
    SPECIALIZATION_WRITE,
    SPECIALIZATION_UPDATE,
    SPECIALIZATION_DELETE,

    // ── Staff ───────────────────────────────
    STAFF_READ,
    STAFF_WRITE,
    STAFF_UPDATE,
    STAFF_DELETE,

    // ── User / Account ──────────────────────
    USER_READ,
    USER_WRITE,
    USER_UPDATE,
    USER_DELETE,

    // ── Patient ─────────────────────────────
    PATIENT_READ,
    PATIENT_WRITE,
    PATIENT_UPDATE,
    PATIENT_DELETE,
}
