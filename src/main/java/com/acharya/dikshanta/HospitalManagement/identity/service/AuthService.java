package com.acharya.dikshanta.HospitalManagement.identity.service;

import com.acharya.dikshanta.HospitalManagement.common.service.JwtService;
import com.acharya.dikshanta.HospitalManagement.identity.dto.request.LoginRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.LoginResponse;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.UserResponse;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.model.UserPrincipal;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginResponse loginUser(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.username());
        UserPrincipal principal = authenticate(loginRequest);
        String token = jwtService.generateToken(principal);

        User user = getUser(principal.getId());
        UserResponse userResponse = buildUserResponse(user);

        log.info("User '{}' authenticated successfully", user.getUsername());
        return buildLoginResponse(userResponse, token);
    }

    private UserPrincipal authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        return (UserPrincipal) authentication.getPrincipal();
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private UserResponse buildUserResponse(User user) {
        Staff staff = user.getStaff();
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(staff != null ? staff.getName() : null)
                .phoneNumber(staff != null ? staff.getPhoneNumber() : null)
                .gender(staff != null ? staff.getGender() : null)
                .build();
    }

    private LoginResponse buildLoginResponse(UserResponse userResponse, String token) {
        return LoginResponse.builder()
                .userResponse(userResponse)
                .token(token)
                .build();
    }
}
