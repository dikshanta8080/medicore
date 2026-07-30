package com.acharya.dikshanta.HospitalManagement.appointment.mapper;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.CreateAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toEntity(CreateAppointmentRequest request) {
        return Appointment.builder()
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .reason(request.reason())
                .build();
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFullName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getStaff().getName())
                .departmentId(appointment.getDepartment().getId())
                .department(appointment.getDepartment().getName())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .reason(appointment.getReason())
                .allergies(appointment.getPatient().getAllergies())
                .appointmentStatus(appointment.getAppointmentStatus())
                .bookedBy(appointment.getBookedBy() != null ? appointment.getBookedBy().getName() : null)
                .build();
    }
}
