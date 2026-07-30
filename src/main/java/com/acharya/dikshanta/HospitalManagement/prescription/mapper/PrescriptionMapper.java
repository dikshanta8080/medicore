package com.acharya.dikshanta.HospitalManagement.prescription.mapper;

import com.acharya.dikshanta.HospitalManagement.prescription.dto.request.PrescriptionItemRequest;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.response.PrescriptionItemResponse;
import com.acharya.dikshanta.HospitalManagement.prescription.dto.response.PrescriptionResponse;
import com.acharya.dikshanta.HospitalManagement.prescription.model.Prescription;
import com.acharya.dikshanta.HospitalManagement.prescription.model.PrescriptionItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrescriptionMapper {

    public PrescriptionItem toItemEntity(PrescriptionItemRequest request) {
        return PrescriptionItem.builder()
                .medicineName(request.medicineName())
                .dosage(request.dosage())
                .frequency(request.frequency())
                .duration(request.duration())
                .instructions(request.instructions())
                .build();
    }

    public List<PrescriptionItem> toItemEntities(List<PrescriptionItemRequest> requests) {
        return requests.stream().map(this::toItemEntity).toList();
    }

    public PrescriptionItemResponse toItemResponse(PrescriptionItem item) {
        return PrescriptionItemResponse.builder()
                .id(item.getId())
                .medicineName(item.getMedicineName())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .instructions(item.getInstructions())
                .build();
    }

    public PrescriptionResponse toResponse(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .consultationId(prescription.getConsultation().getId())
                .items(prescription.getItems().stream().map(this::toItemResponse).toList())
                .build();
    }
}
