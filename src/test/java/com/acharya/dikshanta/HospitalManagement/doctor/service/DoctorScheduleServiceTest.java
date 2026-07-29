package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.FilterDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DoctorScheduleMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Specialization;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorScheduleRepository;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorScheduleServiceTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorScheduleMapper doctorScheduleMapper;

    @InjectMocks
    private DoctorScheduleService doctorScheduleService;

    @Test
    @DisplayName("Should create doctor schedule successfully")
    void testCreateSchedule_Success() {
        UUID doctorId = UUID.randomUUID();
        CreateDoctorScheduleRequest request = new CreateDoctorScheduleRequest(
                doctorId, Days.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)
        );
        Doctor doctor = buildDoctor(doctorId);
        DoctorSchedule schedule = buildSchedule(doctor);
        DoctorScheduleResponse response = buildResponse(schedule);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.existsOverlappingSchedule(
                eq(doctorId), eq(Days.MONDAY), eq(LocalTime.of(9, 0)), eq(LocalTime.of(12, 0)), eq(null)))
                .thenReturn(false);
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(schedule);
        when(doctorScheduleMapper.toResponse(schedule)).thenReturn(response);

        DoctorScheduleResponse result = doctorScheduleService.createSchedule(request);

        assertNotNull(result);
        assertEquals(Days.MONDAY, result.day());
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when start time is not before end time")
    void testCreateSchedule_InvalidTimeRange() {
        UUID doctorId = UUID.randomUUID();
        CreateDoctorScheduleRequest request = new CreateDoctorScheduleRequest(
                doctorId, Days.MONDAY, LocalTime.of(12, 0), LocalTime.of(9, 0)
        );
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(buildDoctor(doctorId)));

        assertThrows(BusinessException.class, () -> doctorScheduleService.createSchedule(request));
        verify(doctorScheduleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException when schedule overlaps")
    void testCreateSchedule_OverlappingSchedule() {
        UUID doctorId = UUID.randomUUID();
        CreateDoctorScheduleRequest request = new CreateDoctorScheduleRequest(
                doctorId, Days.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)
        );
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(buildDoctor(doctorId)));
        when(doctorScheduleRepository.existsOverlappingSchedule(
                eq(doctorId), eq(Days.MONDAY), eq(LocalTime.of(9, 0)), eq(LocalTime.of(12, 0)), eq(null)))
                .thenReturn(true);

        assertThrows(BusinessException.class, () -> doctorScheduleService.createSchedule(request));
        verify(doctorScheduleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fetch schedules with filters")
    void testGetSchedules_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        FilterDoctorScheduleRequest filter = new FilterDoctorScheduleRequest(
                Days.MONDAY, null, null, null, "John", null
        );
        Doctor doctor = buildDoctor(UUID.randomUUID());
        DoctorSchedule schedule = buildSchedule(doctor);
        DoctorScheduleResponse response = buildResponse(schedule);

        when(doctorScheduleRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(schedule)));
        when(doctorScheduleMapper.toResponse(schedule)).thenReturn(response);

        PagedResponse<DoctorScheduleResponse> result = doctorScheduleService.getSchedules(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Dr. John", result.content().getFirst().doctorName());
    }

    @Test
    @DisplayName("Should update doctor schedule successfully")
    void testUpdateSchedule_Success() {
        UUID scheduleId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        UpdateDoctorScheduleRequest request = new UpdateDoctorScheduleRequest(
                scheduleId, Days.TUESDAY, LocalTime.of(10, 0), LocalTime.of(14, 0)
        );
        Doctor doctor = buildDoctor(doctorId);
        DoctorSchedule schedule = buildSchedule(doctor);
        schedule.setId(scheduleId);
        DoctorScheduleResponse response = buildResponse(schedule);

        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(doctorScheduleRepository.existsOverlappingSchedule(
                eq(doctorId), eq(Days.TUESDAY), eq(LocalTime.of(10, 0)), eq(LocalTime.of(14, 0)), eq(scheduleId)))
                .thenReturn(false);
        when(doctorScheduleRepository.save(schedule)).thenReturn(schedule);
        when(doctorScheduleMapper.toResponse(schedule)).thenReturn(response);

        DoctorScheduleResponse result = doctorScheduleService.updateSchedule(request);

        assertNotNull(result);
        assertEquals(Days.TUESDAY, schedule.getDayOfWeek());
        verify(doctorScheduleRepository, times(1)).save(schedule);
    }

    @Test
    @DisplayName("Should delete doctor schedule successfully")
    void testDeleteSchedule_Success() {
        UUID scheduleId = UUID.randomUUID();
        Doctor doctor = buildDoctor(UUID.randomUUID());
        DoctorSchedule schedule = buildSchedule(doctor);
        schedule.setId(scheduleId);

        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        doctorScheduleService.deleteSchedule(scheduleId);

        verify(doctorScheduleRepository, times(1)).delete(schedule);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when schedule is not found")
    void testGetSchedule_NotFound() {
        UUID scheduleId = UUID.randomUUID();
        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorScheduleService.getSchedule(scheduleId));
    }

    private Doctor buildDoctor(UUID doctorId) {
        Staff staff = new Staff();
        staff.setName("Dr. John");

        Department department = Department.builder().name("Cardiology").build();
        Specialization specialization = Specialization.builder().name("Cardiologist").build();

        Doctor doctor = Doctor.builder()
                .consultationFee(java.math.BigDecimal.valueOf(500))
                .licenseNumber("LIC-123")
                .staff(staff)
                .department(department)
                .specialization(specialization)
                .build();
        doctor.setId(doctorId);
        return doctor;
    }

    private DoctorSchedule buildSchedule(Doctor doctor) {
        return DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(Days.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(12, 0))
                .build();
    }

    private DoctorScheduleResponse buildResponse(DoctorSchedule schedule) {
        return DoctorScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctor().getId())
                .doctorName("Dr. John")
                .department("Cardiology")
                .specialization("Cardiologist")
                .day(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
