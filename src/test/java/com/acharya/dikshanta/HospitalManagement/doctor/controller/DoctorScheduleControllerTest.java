package com.acharya.dikshanta.HospitalManagement.doctor.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.service.DoctorScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DoctorScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DoctorScheduleService doctorScheduleService;

    @InjectMocks
    private DoctorScheduleController doctorScheduleController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(doctorScheduleController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("POST /doctor-schedules should create schedule")
    void testCreateSchedule() throws Exception {
        UUID doctorId = UUID.randomUUID();
        UUID scheduleId = UUID.randomUUID();
        CreateDoctorScheduleRequest request = new CreateDoctorScheduleRequest(
                doctorId, Days.SUNDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)
        );

        DoctorScheduleResponse response = DoctorScheduleResponse.builder()
                .id(scheduleId)
                .doctorId(doctorId)
                .doctorName("Dr. Smith")
                .department("Cardiology")
                .specialization("Cardiologist")
                .day(Days.SUNDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .build();

        when(doctorScheduleService.createSchedule(any(CreateDoctorScheduleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/doctor-schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Doctor schedule created successfully"))
                .andExpect(jsonPath("$.data.id").value(scheduleId.toString()))
                .andExpect(jsonPath("$.data.doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$.data.doctorName").value("Dr. Smith"))
                .andExpect(jsonPath("$.data.day").value("SUNDAY"))
                .andExpect(jsonPath("$.data.startTime").value("09:00:00"))
                .andExpect(jsonPath("$.data.endTime").value("17:00:00"));

        verify(doctorScheduleService, times(1)).createSchedule(any(CreateDoctorScheduleRequest.class));
    }

    @Test
    @DisplayName("GET /doctor-schedules should fetch paginated and filtered schedules")
    void testGetSchedules() throws Exception {
        UUID scheduleId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();

        DoctorScheduleResponse response = DoctorScheduleResponse.builder()
                .id(scheduleId)
                .doctorId(doctorId)
                .doctorName("Dr. Smith")
                .department("Cardiology")
                .specialization("Cardiologist")
                .day(Days.SUNDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .build();

        PagedResponse<DoctorScheduleResponse> pagedResponse = PagedResponse.toPagedResponse(
                new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1)
        );

        when(doctorScheduleService.getSchedules(any(), any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/doctor-schedules")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("day", "SUNDAY")
                        .param("doctorId", doctorId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Doctor schedules fetched successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(scheduleId.toString()))
                .andExpect(jsonPath("$.data.content[0].day").value("SUNDAY"));

        verify(doctorScheduleService, times(1)).getSchedules(any(), any(Pageable.class));
    }

    @Test
    @DisplayName("GET /doctor-schedules/{id} should return schedule by id")
    void testGetScheduleById() throws Exception {
        UUID scheduleId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();

        DoctorScheduleResponse response = DoctorScheduleResponse.builder()
                .id(scheduleId)
                .doctorId(doctorId)
                .doctorName("Dr. Smith")
                .department("Cardiology")
                .specialization("Cardiologist")
                .day(Days.MONDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(16, 0))
                .build();

        when(doctorScheduleService.getSchedule(scheduleId)).thenReturn(response);

        mockMvc.perform(get("/doctor-schedules/{id}", scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Doctor schedule fetched successfully"))
                .andExpect(jsonPath("$.data.id").value(scheduleId.toString()))
                .andExpect(jsonPath("$.data.day").value("MONDAY"));

        verify(doctorScheduleService, times(1)).getSchedule(scheduleId);
    }

    @Test
    @DisplayName("PUT /doctor-schedules should update schedule")
    void testUpdateSchedule() throws Exception {
        UUID scheduleId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        UpdateDoctorScheduleRequest request = new UpdateDoctorScheduleRequest(
                scheduleId, Days.TUESDAY, LocalTime.of(10, 0), LocalTime.of(18, 0)
        );

        DoctorScheduleResponse response = DoctorScheduleResponse.builder()
                .id(scheduleId)
                .doctorId(doctorId)
                .doctorName("Dr. Smith")
                .department("Cardiology")
                .specialization("Cardiologist")
                .day(Days.TUESDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(18, 0))
                .build();

        when(doctorScheduleService.updateSchedule(any(UpdateDoctorScheduleRequest.class))).thenReturn(response);

        mockMvc.perform(put("/doctor-schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Doctor schedule updated successfully"))
                .andExpect(jsonPath("$.data.id").value(scheduleId.toString()))
                .andExpect(jsonPath("$.data.day").value("TUESDAY"));

        verify(doctorScheduleService, times(1)).updateSchedule(any(UpdateDoctorScheduleRequest.class));
    }

    @Test
    @DisplayName("DELETE /doctor-schedules/{id} should delete schedule")
    void testDeleteSchedule() throws Exception {
        UUID scheduleId = UUID.randomUUID();
        doNothing().when(doctorScheduleService).deleteSchedule(scheduleId);

        mockMvc.perform(delete("/doctor-schedules/{id}", scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Doctor schedule deleted successfully"));

        verify(doctorScheduleService, times(1)).deleteSchedule(scheduleId);
    }
}
