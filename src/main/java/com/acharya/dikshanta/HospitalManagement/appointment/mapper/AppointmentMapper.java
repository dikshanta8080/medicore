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
                .patientId(appointment.getPatient() != null ? appointment.getPatient().getId() : null)
                .patientName(appointment.getPatient() != null ? appointment.getPatient().getFullName() : "Unknown Patient")
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null)
                .doctorName(appointment.getDoctor() != null && appointment.getDoctor().getStaff() != null ? appointment.getDoctor().getStaff().getName() : "Unknown Doctor")
                .departmentId(appointment.getDepartment() != null ? appointment.getDepartment().getId() : null)
                .department(appointment.getDepartment() != null ? appointment.getDepartment().getName() : "General")
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .reason(appointment.getReason())
                .allergies(appointment.getPatient() != null ? appointment.getPatient().getAllergies() : null)
                .appointmentStatus(appointment.getAppointmentStatus())
                .bookedBy(appointment.getBookedBy() != null ? appointment.getBookedBy().getName() : null)
                .build();
    }
}
