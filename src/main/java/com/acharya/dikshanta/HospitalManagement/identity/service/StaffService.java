package com.acharya.dikshanta.HospitalManagement.identity.service;

import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.CreateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.StaffResponse;
import com.acharya.dikshanta.HospitalManagement.identity.mapper.StaffMapper;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffMapper staffMapper;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {
        checkIfStaffAlreadyExists(request);
        User user = buildUser(request);
        Staff staff = staffMapper.toStaff(request);
        staff.setUser(userRepository.save(user));
        return staffMapper.toResponse(staffRepository.save(staff));
    }


    private void checkIfStaffAlreadyExists(CreateStaffRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Staff Already Exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Staff Already Exists");
        }
    }

    private User buildUser(CreateStaffRequest request) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.RECEPTIONIST)
                .build();
    }

}
