package com.acharya.dikshanta.HospitalManagement.patient.mapper;

import com.acharya.dikshanta.HospitalManagement.patient.dto.request.CreatePatientRequest;
import com.acharya.dikshanta.HospitalManagement.patient.dto.response.PatientResponse;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toEntity(CreatePatientRequest request);

    PatientResponse toResponse(Patient patient);
}
