package com.acharya.dikshanta.HospitalManagement.consultation.mapper;

import com.acharya.dikshanta.HospitalManagement.consultation.dto.request.CreateConsultationRequest;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.response.ConsultationResponse;
import com.acharya.dikshanta.HospitalManagement.consultation.model.Consultation;
import org.springframework.stereotype.Component;

@Component
public class ConsultationMapper {

    public Consultation toEntity(CreateConsultationRequest request) {
        return Consultation.builder()
                .symptoms(request.symptoms())
                .diagnosis(request.diagnosis())
                .clinicalNotes(request.clinicalNotes())
                .followUpDate(request.followUpDate())
                .build();
    }

    public ConsultationResponse toResponse(Consultation consultation) {
        return ConsultationResponse.builder()
                .id(consultation.getId())
                .appointmentId(consultation.getAppointment().getId())
                .symptoms(consultation.getSymptoms())
                .diagnosis(consultation.getDiagnosis())
                .clinicalNotes(consultation.getClinicalNotes())
                .followUpDate(consultation.getFollowUpDate())
                .build();
    }
}
