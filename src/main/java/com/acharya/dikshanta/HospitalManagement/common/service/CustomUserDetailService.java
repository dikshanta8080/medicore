package com.acharya.dikshanta.HospitalManagement.common.service;

import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.model.UserPrincipal;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("username not found")));
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .staffId(user.getStaff().getId())
                .role(user.getRole())
                .build();
    }
}
