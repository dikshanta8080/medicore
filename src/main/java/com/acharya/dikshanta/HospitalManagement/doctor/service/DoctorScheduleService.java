package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.FilterDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DoctorScheduleMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    @Transactional
    public DoctorScheduleResponse createSchedule(CreateDoctorScheduleRequest request) {
        DoctorSchedule doctorSchedule = doctorScheduleMapper.toEntity(request);
        return doctorScheduleMapper.toResponse(doctorScheduleRepository.save(doctorSchedule));
    }

    @Transactional
    public PagedResponse<DoctorSchedule> getDoctor(PaginationRequest request, FilterDoctorScheduleRequest filterDoctorScheduleRequest) {
        return null;
    }
}
