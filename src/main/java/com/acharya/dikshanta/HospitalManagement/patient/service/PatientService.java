package com.acharya.dikshanta.HospitalManagement.patient.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.enums.BloodGroup;
import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.patient.dto.request.CreatePatientRequest;
import com.acharya.dikshanta.HospitalManagement.patient.dto.response.PatientResponse;
import com.acharya.dikshanta.HospitalManagement.patient.mapper.PatientMapper;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.acharya.dikshanta.HospitalManagement.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponse create(CreatePatientRequest request) {
        if (patientRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BusinessException("Phone number already exists.");
        }
        if (patientRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already exists.");
        }
        Patient patient = patientMapper.toEntity(request);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toResponse(savedPatient);
    }

    @Transactional
    public PatientResponse update(CreatePatientRequest request, UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Patient not found with the provided id"));
        if (request.phoneNumber() != null) {
            patient.setPhoneNumber(request.phoneNumber());
        }
        if (request.allergies() != null) {
            patient.setAllergies(request.allergies());
        }
        if (request.medicalHistory() != null) {
            patient.setMedicalHistory(request.medicalHistory());
        }
        if (request.fullName() != null) {
            patient.setFullName(request.fullName());
        }
        return patientMapper.toResponse(patient);
    }

    @Transactional
    public PagedResponse<PatientResponse> getAllPatients(String patientNumber,
                                                         String fullName,
                                                         String phoneNumber,
                                                         Gender gender,
                                                         BloodGroup bloodGroup,
                                                         Pageable pageable) {
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        Page<PatientResponse> responsePage = patientPage.map(patientMapper::toResponse);
        return PagedResponse.toPagedResponse(responsePage);
    }

    @Transactional
    public PatientResponse getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with the provided id"));
        return patientMapper.toResponse(patient);
    }

    @Transactional
    public void deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with the provided id"));
        patient.performSoftDelete();
        patientRepository.save(patient);
    }

}
