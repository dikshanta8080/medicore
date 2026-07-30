package com.acharya.dikshanta.HospitalManagement.prescription.service;

import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.consultation.model.Consultation;
import com.acharya.dikshanta.HospitalManagement.consultation.repository.ConsultationRepository;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.request.CreatePrescriptionRequest;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.request.UpdatePrescriptionRequest;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.response.PrescriptionResponse;
import com.acharya.dikshanta.HospitalManagement.prescription.mapper.PrescriptionMapper;
import com.acharya.dikshanta.HospitalManagement.prescription.model.Prescription;
import com.acharya.dikshanta.HospitalManagement.prescription.model.PrescriptionItem;
import com.acharya.dikshanta.HospitalManagement.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Transactional
    public PrescriptionResponse createPrescription(UUID consultationId, CreatePrescriptionRequest request) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

        if (prescriptionRepository.existsByConsultationId(consultationId)) {
            throw new BusinessException("Prescription already exists for this consultation");
        }

        Prescription prescription = Prescription.builder()
                .consultation(consultation)
                .build();

        List<PrescriptionItem> items = prescriptionMapper.toItemEntities(request.items());
        items.forEach(item -> item.setPrescription(prescription));
        prescription.getItems().addAll(items);

        return prescriptionMapper.toResponse(prescriptionRepository.save(prescription));
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescription(UUID id) {
        return prescriptionMapper.toResponse(findPrescription(id));
    }

    @Transactional
    public PrescriptionResponse updatePrescription(UUID id, UpdatePrescriptionRequest request) {
        Prescription prescription = findPrescription(id);

        prescription.getItems().clear();
        List<PrescriptionItem> items = prescriptionMapper.toItemEntities(request.items());
        items.forEach(item -> item.setPrescription(prescription));
        prescription.getItems().addAll(items);

        return prescriptionMapper.toResponse(prescriptionRepository.save(prescription));
    }

    @Transactional
    public void deletePrescription(UUID id) {
        Prescription prescription = findPrescription(id);
        prescription.performSoftDelete();
        prescriptionRepository.save(prescription);
    }

    private Prescription findPrescription(UUID id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
    }
}
