package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.CreateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.mapper.DoctorMapper;
import com.acharya.dikshanta.HospitalManagement.identity.mapper.StaffMapper;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import com.acharya.dikshanta.HospitalManagement.identity.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final StaffService staffService;
    private final StaffMapper staffMapper;
    private final DoctorMapper doctorMapper;

    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        checkIfStaffAlreadyExists(request);
        CreateStaffRequest staffRequest = buildStaffRequest(request);
        Staff staff = staffService.saveStaff(staffRequest);
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setStaff(staff);
        return doctorMapper.toResponse(doctorRepository.save(doctor));

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
