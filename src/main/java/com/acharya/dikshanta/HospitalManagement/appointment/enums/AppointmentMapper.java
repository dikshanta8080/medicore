package com.acharya.dikshanta.HospitalManagement.appointment.enums;

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
                .patientName(appointment.getPatient().getFullName())
                .doctorName(appointment.getDoctor().getStaff().getName())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .department(appointment.getDepartment().getName())
                .reason(appointment.getReason())
                .allergies(appointment.getPatient().getAllergies())
                .bookedBy(appointment.getBookedBy().getName())
                .build();
    }
}
