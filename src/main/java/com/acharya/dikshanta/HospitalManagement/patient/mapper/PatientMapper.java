package com.acharya.dikshanta.HospitalManagement.patient.mapper;

import com.acharya.dikshanta.HospitalManagement.patient.dto.request.CreatePatientRequest;
import com.acharya.dikshanta.HospitalManagement.patient.dto.response.PatientResponse;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    public Patient toEntity(CreatePatientRequest request);
    public PatientResponse toResponse(Patient patient);
}
