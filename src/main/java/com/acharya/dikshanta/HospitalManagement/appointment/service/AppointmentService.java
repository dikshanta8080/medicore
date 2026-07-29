package com.acharya.dikshanta.HospitalManagement.appointment.service;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.CreateAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.RescheduleAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentMapper;
import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import com.acharya.dikshanta.HospitalManagement.appointment.repository.AppointmentRepository;
import com.acharya.dikshanta.HospitalManagement.common.LoggedInUser;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DepartmentRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.acharya.dikshanta.HospitalManagement.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;
    private final DepartmentRepository departmentRepository;
    private final StaffRepository staffRepository;
    private PatientRepository patientRepository;

    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        Patient patient = getPatient(request);
        Doctor doctor = getDoctor(request);
        Department department = getDepartment(request);
        Staff staff = getStaff();
        var appointment = appointmentMapper.toEntity(request);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAppointmentStatus(AppointmentStatus.BOOKED);
        appointment.setBookedBy(staff);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));

    }

    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId) {
        Appointment appointment = getAppointment(appointmentId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        return appointmentMapper.toResponse(appointment);
    }

    private Appointment getAppointment(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(RescheduleAppointmentRequest request) {
        Appointment appointment = getAppointment(request.appointmentId());
        appointment.setAppointmentStatus(request.appointmentStatus());
        return appointmentMapper.toResponse(appointment);
    }

    private Staff getStaff() {
        return staffRepository.findById(LoggedInUser.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    }

    private Department getDepartment(CreateAppointmentRequest request) {
        return departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    private Doctor getDoctor(CreateAppointmentRequest request) {
        return doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private Patient getPatient(CreateAppointmentRequest request) {
        return patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }
}
