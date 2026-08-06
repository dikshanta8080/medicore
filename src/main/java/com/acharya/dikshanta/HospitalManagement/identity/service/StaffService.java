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
    private final com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository doctorRepository;

    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {
        checkIfStaffAlreadyExists(request);
        Role assignedRole = request.role() != null ? request.role() : Role.RECEPTIONIST;
        User user = buildUser(request, assignedRole);
        Staff staff = staffMapper.toStaff(request);
        staff.setUser(userRepository.save(user));
        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Transactional
    public Staff saveStaff(CreateStaffRequest request) {
        return saveStaff(request, Role.RECEPTIONIST);
    }

    @Transactional
    public Staff saveStaff(CreateStaffRequest request, Role role) {
        checkIfStaffAlreadyExists(request);
        User user = buildUser(request, role);
        Staff staff = staffMapper.toStaff(request);
        staff.setUser(userRepository.save(user));
        return staffRepository.save(staff);
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
        User user = staff.getUser();

        // Detach doctor from staff before deletion to avoid FK constraint issues
        doctorRepository.findByStaffId(id).ifPresent(doctor -> {
            doctor.setStaff(null);
            doctorRepository.save(doctor);
            doctorRepository.delete(doctor);
        });

        // Detach user reference from staff before deleting staff
        staff.setUser(null);
        staffRepository.save(staff);
        staffRepository.delete(staff);

        if (user != null) {
            try {
                userRepository.delete(user);
            } catch (Exception e) {
                // Soft-delete user if referenced in audit trails
                user.setDeleted(true);
                userRepository.save(user);
            }
        }
    }

    private void checkIfStaffAlreadyExists(CreateStaffRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Staff Already Exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Staff Already Exists");
        }
    }

    private User buildUser(CreateStaffRequest request, Role role) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
    }

}
