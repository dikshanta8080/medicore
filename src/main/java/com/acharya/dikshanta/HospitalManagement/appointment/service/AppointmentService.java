package com.acharya.dikshanta.HospitalManagement.appointment.service;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.CreateAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.RescheduleAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentResponse createAppointment(CreateAppointmentRequest request);

    AppointmentResponse getAppointmentById(UUID appointmentId);

    List<AppointmentResponse> getTodayAppointments();

    List<AppointmentResponse> getAppointmentsByDate(LocalDate date);

    List<AppointmentResponse> getTodayAppointmentsByDoctorId(UUID doctorId);

    AppointmentResponse checkInAppointment(UUID appointmentId);

    AppointmentResponse cancelAppointment(UUID appointmentId);

    AppointmentResponse rescheduleAppointment(UUID appointmentId, RescheduleAppointmentRequest request);
}
