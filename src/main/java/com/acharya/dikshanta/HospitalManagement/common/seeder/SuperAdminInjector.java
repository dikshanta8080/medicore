package com.acharya.dikshanta.HospitalManagement.common.seeder;

import com.acharya.dikshanta.HospitalManagement.common.config.AppData;
import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperAdminInjector implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AppData appData;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        AppData.Superadmin superadmin = appData.getSuperadmin();
        if (!userRepository.existsByRole(Role.SUPER_ADMIN)) {
            User user = User.builder()
                    .username(superadmin.getUsername())
                    .email(superadmin.getEmail())
                    .password(passwordEncoder.encode(superadmin.getPassword()))
                    .role(Role.SUPER_ADMIN)
                    .build();
            userRepository.save(user);
        }
    }
}
