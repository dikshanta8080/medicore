package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.common.specifications.DoctorScheduleSpecification;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.FilterDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DoctorScheduleMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    @Transactional
    public DoctorScheduleResponse createSchedule(CreateDoctorScheduleRequest request) {
        Doctor doctor = findDoctor(request.doctorId());
        validateTimeRange(request.startTime(), request.endTime());
        validateNoOverlap(doctor.getId(), request.dayOfWeek(), request.startTime(), request.endTime(), null);

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();

        return doctorScheduleMapper.toResponse(doctorScheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public PagedResponse<DoctorScheduleResponse> getSchedules(FilterDoctorScheduleRequest filter, Pageable pageable) {
        Specification<DoctorSchedule> spec = DoctorScheduleSpecification.filterSchedules(filter);
        Page<DoctorScheduleResponse> responsePage = doctorScheduleRepository.findAll(spec, pageable)
                .map(doctorScheduleMapper::toResponse);
        return PagedResponse.toPagedResponse(responsePage);
    }

    @Transactional(readOnly = true)
    public DoctorScheduleResponse getSchedule(UUID id) {
        return doctorScheduleMapper.toResponse(findSchedule(id));
    }

    @Transactional
    public DoctorScheduleResponse updateSchedule(UpdateDoctorScheduleRequest request) {
        DoctorSchedule schedule = findSchedule(request.scheduleId());

        var day = request.dayOfWeek() != null ? request.dayOfWeek() : schedule.getDayOfWeek();
        var startTime = request.startTime() != null ? request.startTime() : schedule.getStartTime();
        var endTime = request.endTime() != null ? request.endTime() : schedule.getEndTime();

        validateTimeRange(startTime, endTime);
        validateNoOverlap(schedule.getDoctor().getId(), day, startTime, endTime, schedule.getId());

        if (request.dayOfWeek() != null) {
            schedule.setDayOfWeek(request.dayOfWeek());
        }
        if (request.startTime() != null) {
            schedule.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            schedule.setEndTime(request.endTime());
        }

        return doctorScheduleMapper.toResponse(doctorScheduleRepository.save(schedule));
    }

    @Transactional
    public void deleteSchedule(UUID id) {
        DoctorSchedule schedule = findSchedule(id);
        doctorScheduleRepository.delete(schedule);
    }

    private Doctor findDoctor(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private DoctorSchedule findSchedule(UUID id) {
        return doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found"));
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new BusinessException("Start time must be before end time");
        }
    }

    private void validateNoOverlap(UUID doctorId, Days day, LocalTime startTime, LocalTime endTime, UUID excludeId) {
        if (doctorScheduleRepository.existsOverlappingSchedule(doctorId, day, startTime, endTime, excludeId)) {
            throw new BusinessException("Schedule overlaps with an existing slot for this doctor");
        }
    }
}
