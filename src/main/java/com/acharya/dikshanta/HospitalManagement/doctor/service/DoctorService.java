package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.appointment.dto.response.AppointmentResponse;
import com.acharya.dikshanta.HospitalManagement.appointment.service.AppointmentService;
import com.acharya.dikshanta.HospitalManagement.common.LoggedInUser;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.common.specifications.DoctorSpecification;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.DoctorFilterRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DoctorMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Specialization;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DepartmentRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.SpecializationRepository;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.CreateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.mapper.StaffMapper;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import com.acharya.dikshanta.HospitalManagement.identity.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final SpecializationRepository specializationRepository;
    private final StaffService staffService;
    private final StaffMapper staffMapper;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyTodayAppointments() {
        Doctor doctor = doctorRepository.findByStaffId(LoggedInUser.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for the logged-in user"));
        return appointmentService.getTodayAppointmentsByDoctorId(doctor.getId());
    }

    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        checkIfStaffAlreadyExists(request);
        CreateStaffRequest staffRequest = buildStaffRequest(request);
        Staff staff = staffService.saveStaff(staffRequest);
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setStaff(staff);
        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional(readOnly = true)
    public PagedResponse<DoctorResponse> getDoctors(DoctorFilterRequest filter, Pageable pageable) {
        Specification<Doctor> spec = DoctorSpecification.filterDoctors(filter);
        Page<DoctorResponse> responsePage = doctorRepository.findAll(spec, pageable)
                .map(doctorMapper::toResponse);
        return PagedResponse.toPagedResponse(responsePage);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctor(UUID id) {
        Doctor doctor = findDoctor(id);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public DoctorResponse updateDoctor(UpdateDoctorRequest request) {
        Doctor doctor = findDoctor(request.doctorId());
        Staff staff = doctor.getStaff();

        if (request.name() != null) {
            staff.setName(request.name());
        }
        if (request.address() != null) {
            staff.setAddress(request.address());
        }
        if (request.phoneNumber() != null) {
            staff.setPhoneNumber(request.phoneNumber());
        }
        if (request.consultationFee() != null) {
            doctor.setConsultationFee(request.consultationFee());
        }
        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            doctor.setDepartment(department);
        }
        if (request.specializationId() != null) {
            Specialization specialization = specializationRepository.findById(request.specializationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
            doctor.setSpecialization(specialization);
        }
        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        Doctor doctor = findDoctor(id);
        doctor.performSoftDelete();
        doctorRepository.save(doctor);
    }

    private Doctor findDoctor(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private void checkIfStaffAlreadyExists(CreateDoctorRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Staff Already Exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Staff Already Exists");
        }
    }

    private CreateStaffRequest buildStaffRequest(CreateDoctorRequest request) {
        return CreateStaffRequest.builder()
                .name(request.name())
                .email(request.email())
                .username(request.username())
                .password(request.password())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .gender(request.gender())
                .build();
    }
}

