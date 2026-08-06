package com.acharya.dikshanta.HospitalManagement.identity.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.UpdateProfileRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.UserResponse;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Staff staff = user.getStaff();
        UserResponse response = UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .name(staff != null ? staff.getName() : null)
                .phoneNumber(staff != null ? staff.getPhoneNumber() : null)
                .gender(staff != null ? staff.getGender() : null)
                .build();
        return ResponseEntity.ok(ApiResponse.success(response, "Profile fetched successfully"));
    }

    @PutMapping("/profile")
    @Transactional
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Staff staff = user.getStaff();

        // Update email on user
        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email());
            userRepository.save(user);
        }

        // Update name on staff profile
        if (request.name() != null && !request.name().isBlank() && staff != null) {
            staff.setName(request.name());
            staffRepository.save(staff);
        }

        UserResponse response = UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .name(staff != null ? staff.getName() : null)
                .phoneNumber(staff != null ? staff.getPhoneNumber() : null)
                .gender(staff != null ? staff.getGender() : null)
                .build();
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }
}
