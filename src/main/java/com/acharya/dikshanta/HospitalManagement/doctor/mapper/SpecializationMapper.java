package com.acharya.dikshanta.HospitalManagement.doctor.mapper;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateSpecializationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.SpecializationResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Specialization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecializationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    Specialization toEntity(CreateSpecializationRequest request);

    SpecializationResponse toResponse(Specialization specialization);
}
