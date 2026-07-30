package com.acharya.dikshanta.HospitalManagement.consultation.service;

import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import com.acharya.dikshanta.HospitalManagement.appointment.repository.AppointmentRepository;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.request.CreateConsultationRequest;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.request.UpdateConsultationRequest;
import com.acharya.dikshanta.HospitalManagement.consultation.dto.response.ConsultationResponse;
import com.acharya.dikshanta.HospitalManagement.consultation.mapper.ConsultationMapper;
import com.acharya.dikshanta.HospitalManagement.consultation.model.Consultation;
import com.acharya.dikshanta.HospitalManagement.consultation.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationMapper consultationMapper;

    @Transactional
    public ConsultationResponse startConsultation(UUID appointmentId, CreateConsultationRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getAppointmentStatus() != AppointmentStatus.CHECKED_IN) {
            throw new BusinessException("Consultation can only be started for checked-in appointments");
        }
        if (consultationRepository.existsByAppointmentId(appointmentId)) {
            throw new BusinessException("Consultation already exists for this appointment");
        }

        Consultation consultation = consultationMapper.toEntity(request);
        consultation.setAppointment(appointment);

        appointment.setAppointmentStatus(AppointmentStatus.CONSULTING);

        consultationRepository.save(consultation);
        appointmentRepository.save(appointment);

        return consultationMapper.toResponse(consultation);
    }

    @Transactional(readOnly = true)
    public ConsultationResponse getConsultation(UUID id) {
        return consultationMapper.toResponse(findConsultation(id));
    }

    @Transactional
    public ConsultationResponse updateConsultation(UUID id, UpdateConsultationRequest request) {
        Consultation consultation = findConsultation(id);

        if (request.symptoms() != null) {
            consultation.setSymptoms(request.symptoms());
        }
        if (request.diagnosis() != null) {
            consultation.setDiagnosis(request.diagnosis());
        }
        if (request.clinicalNotes() != null) {
            consultation.setClinicalNotes(request.clinicalNotes());
        }
        if (request.followUpDate() != null) {
            consultation.setFollowUpDate(request.followUpDate());
        }

        return consultationMapper.toResponse(consultationRepository.save(consultation));
    }

    @Transactional
    public ConsultationResponse completeConsultation(UUID id) {
        Consultation consultation = findConsultation(id);
        Appointment appointment = consultation.getAppointment();

        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return consultationMapper.toResponse(consultation);
    }

    private Consultation findConsultation(UUID id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));
    }
}
