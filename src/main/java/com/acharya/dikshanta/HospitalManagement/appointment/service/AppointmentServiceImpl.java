package com.acharya.dikshanta.HospitalManagement.appointment.service;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.CreateAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.RescheduleAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import com.acharya.dikshanta.HospitalManagement.appointment.event.AppointmentBookedEvent;
import com.acharya.dikshanta.HospitalManagement.appointment.mapper.AppointmentMapper;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import com.acharya.dikshanta.HospitalManagement.appointment.repository.AppointmentRepository;
import com.acharya.dikshanta.HospitalManagement.common.LoggedInUser;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DepartmentRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorScheduleRepository;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.acharya.dikshanta.HospitalManagement.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final EnumSet<AppointmentStatus> INACTIVE_STATUSES =
            EnumSet.of(AppointmentStatus.CANCELLED, AppointmentStatus.NO_SHOW);

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;
    private final DepartmentRepository departmentRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        Patient patient = getPatient(request.patientId());
        Doctor doctor = getDoctor(request.doctorId());
        Department department = getDepartment(request.departmentId());
        Staff staff = getStaff();

        validateDoctorAvailability(doctor.getId(), request.appointmentDate(), request.appointmentTime());
        validateNoConflictingAppointment(doctor.getId(), request.appointmentDate(), request.appointmentTime(), null);

        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAppointmentStatus(AppointmentStatus.BOOKED);
        appointment.setBookedBy(staff);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        applicationEventPublisher.publishEvent(new AppointmentBookedEvent(savedAppointment.getId()));

        return appointmentMapper.toResponse(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(UUID appointmentId) {
        return appointmentMapper.toResponse(findAppointment(appointmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getTodayAppointments() {
        return getAppointmentsByDate(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDateOrderByAppointmentTimeAsc(date).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getTodayAppointmentsByDoctorId(UUID doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found");
        }
        return appointmentRepository
                .findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctorId, LocalDate.now()).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentResponse checkInAppointment(UUID appointmentId) {
        Appointment appointment = findAppointment(appointmentId);

        if (appointment.getAppointmentStatus() != AppointmentStatus.BOOKED) {
            throw new BusinessException("Only booked appointments can be checked in");
        }
        if (!appointment.getAppointmentDate().equals(LocalDate.now())) {
            throw new BusinessException("Appointment can only be checked in on the scheduled date");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CHECKED_IN);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId) {
        Appointment appointment = findAppointment(appointmentId);

        if (appointment.getAppointmentStatus() != AppointmentStatus.BOOKED) {
            throw new BusinessException("Only booked appointments can be cancelled");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointment(UUID appointmentId, RescheduleAppointmentRequest request) {
        Appointment appointment = findAppointment(appointmentId);

        if (appointment.getAppointmentStatus() != AppointmentStatus.BOOKED) {
            throw new BusinessException("Only booked appointments can be rescheduled");
        }

        validateDoctorAvailability(
                appointment.getDoctor().getId(),
                request.appointmentDate(),
                request.appointmentTime());
        validateNoConflictingAppointment(
                appointment.getDoctor().getId(),
                request.appointmentDate(),
                request.appointmentTime(),
                appointment.getId());

        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setAppointmentTime(request.appointmentTime());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public void completeAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        Appointment savedAppointment = appointmentRepository.save(appointment);


    }



    private Appointment findAppointment(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    private Staff getStaff() {
        UUID staffId = LoggedInUser.getStaffId();
        if (staffId == null) {
            throw new BusinessException("Logged-in user is not linked to a staff profile");
        }

        return staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    }

    private Department getDepartment(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    private Doctor getDoctor(UUID doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private Patient getPatient(UUID patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    private void validateDoctorAvailability(UUID doctorId, LocalDate date, LocalTime time) {
        Days dayOfWeek = toDays(date);
        if (!doctorScheduleRepository.isDoctorAvailableAt(doctorId, dayOfWeek, time)) {
            throw new BusinessException("Doctor is not available at the requested date and time");
        }
    }

    private void validateNoConflictingAppointment(
            UUID doctorId, LocalDate date, LocalTime time, UUID excludeAppointmentId) {
        if (appointmentRepository.existsConflictingAppointment(
                doctorId, date, time, excludeAppointmentId, INACTIVE_STATUSES)) {
            throw new BusinessException("Doctor already has an appointment at the requested date and time");
        }
    }

    private Days toDays(LocalDate date) {
        return Days.values()[date.getDayOfWeek().getValue() % 7];
    }
}
