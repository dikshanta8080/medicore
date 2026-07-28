package com.acharya.dikshanta.HospitalManagement.identity.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.CreateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.UpdateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.StaffResponse;
import com.acharya.dikshanta.HospitalManagement.identity.mapper.StaffMapper;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Transactional
    public Staff saveStaff(CreateStaffRequest request) {
        checkIfStaffAlreadyExists(request);
        User user = buildUser(request);
        Staff staff = staffMapper.toStaff(request);
        staff.setUser(userRepository.save(user));
        return staff;
    }

    @Transactional(readOnly = true)
    public StaffResponse getStaff(UUID id) {
        Staff staff = findStaff(id);
        return staffMapper.toResponse(staff);
    }

    private Staff findStaff(UUID id) {
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff Not Found"));
    }

    @Transactional(readOnly = true)
    public PagedResponse<StaffResponse> getStaffs(Pageable pageable) {
        Page<StaffResponse> responsePage = staffRepository.findAll(pageable).map(staffMapper::toResponse);
        return PagedResponse.toPagedResponse(responsePage);

    }

    @Transactional
    public StaffResponse updateStaff(UpdateStaffRequest request) {

        Staff staff = findStaff(request.staffId());
        if (request.name() != null) {
            staff.setName(request.name());
        }
        if (request.address() != null) {
            staff.setAddress(request.address());
        }
        if (request.phoneNumber() != null) {
            staff.setPhoneNumber(request.phoneNumber());
        }
        return staffMapper.toResponse(staff);
    }

    @Transactional
    public void deleteStaff(UUID id) {
        Staff staff = findStaff(id);
        if (staff.getUser() != null) {
            userRepository.delete(staff.getUser());
        }
        staffRepository.delete(staff);
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
