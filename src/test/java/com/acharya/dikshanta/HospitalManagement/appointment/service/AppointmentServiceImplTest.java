package com.acharya.dikshanta.HospitalManagement.appointment.service;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.request.CreateAppointmentRequest;
import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import com.acharya.dikshanta.HospitalManagement.appointment.mapper.AppointmentMapper;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import com.acharya.dikshanta.HospitalManagement.appointment.repository.AppointmentRepository;
import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DepartmentRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorScheduleRepository;
import com.acharya.dikshanta.HospitalManagement.identity.model.UserPrincipal;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.acharya.dikshanta.HospitalManagement.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUpSecurityContext() {
        UserPrincipal principal = UserPrincipal.builder()
                .id(UUID.randomUUID())
                .username("superadmin")
                .role(Role.SUPER_ADMIN)
                .staffId(null)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createAppointment_allowsNullStaffId() {
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        LocalDate appointmentDate = LocalDate.of(2026, 8, 7);
        LocalTime appointmentTime = LocalTime.of(9, 30);

        CreateAppointmentRequest request = new CreateAppointmentRequest(
                doctorId,
                patientId,
                departmentId,
                "Routine checkup",
                appointmentDate,
                appointmentTime
        );

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setFullName("Jane Doe");

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Department department = new Department();
        department.setId(departmentId);
        department.setName("Laboratory");
        doctor.setDepartment(department);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .department(department)
                .appointmentDate(appointmentDate)
                .appointmentTime(appointmentTime)
                .reason("Routine checkup")
                .appointmentStatus(AppointmentStatus.BOOKED)
                .build();

        AppointmentResponse response = new AppointmentResponse(
                UUID.randomUUID(),
                patientId,
                "Jane Doe",
                doctorId,
                "Dr. John Doe",
                departmentId,
                "Laboratory",
                appointmentTime,
                appointmentDate,
                "Routine checkup",
                null,
                AppointmentStatus.BOOKED,
                null
        );

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(doctorScheduleRepository.isDoctorAvailableAt(eq(doctorId), eq(Days.FRIDAY), eq(appointmentTime))).thenReturn(true);
        when(appointmentRepository.existsConflictingAppointment(eq(doctorId), eq(appointmentDate), eq(appointmentTime), eq(null), any())).thenReturn(false);
        when(appointmentMapper.toEntity(request)).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(appointmentMapper.toResponse(any(Appointment.class))).thenReturn(response);

        AppointmentResponse result = appointmentService.createAppointment(request);

        assertNotNull(result);
        assertNull(result.bookedBy());
    }
}
