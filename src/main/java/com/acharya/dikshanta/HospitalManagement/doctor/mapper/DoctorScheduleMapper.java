package com.acharya.dikshanta.HospitalManagement.doctor.mapper;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {
    DoctorSchedule toEntity(CreateDoctorScheduleRequest request);

    DoctorScheduleResponse toResponse(DoctorSchedule doctorSchedule);

}
