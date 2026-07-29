package com.acharya.dikshanta.HospitalManagement.appointment.service;

import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
}
