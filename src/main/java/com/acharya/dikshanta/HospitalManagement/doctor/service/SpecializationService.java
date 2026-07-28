package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateSpecializationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.SpecializationResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.SpecializationMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Specialization;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;

    @Transactional
    public SpecializationResponse createSpecialization(CreateSpecializationRequest request) {
        checkIfAlreadyExists(request);
        Specialization specialization = specializationMapper.toEntity(request);
        return specializationMapper.toResponse(specializationRepository.save(specialization));

    }

    @Transactional(readOnly = true)
    public List<SpecializationResponse> getSpecializations() {
        return specializationRepository.findAll().stream().map(specializationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SpecializationResponse getSpecialization(UUID id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
        return specializationMapper.toResponse(specialization);
    }

    @Transactional
    public void deleteSpecialization(UUID id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
        specializationRepository.delete(specialization);
    }

    private void checkIfAlreadyExists(CreateSpecializationRequest request) {
        if (specializationRepository.existsByName(request.name())) {
            throw new BusinessException("Specialization already exists");
        }
    }
}
