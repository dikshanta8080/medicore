package com.acharya.dikshanta.HospitalManagement.patient.mapper;

import com.acharya.dikshanta.HospitalManagement.patient.dto.request.CreatePatientRequest;
import com.acharya.dikshanta.HospitalManagement.patient.dto.response.PatientResponse;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Patient toEntity(CreatePatientRequest request);

    PatientResponse toResponse(Patient patient);
}
