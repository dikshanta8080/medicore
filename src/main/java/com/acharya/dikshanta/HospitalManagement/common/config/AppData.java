package com.acharya.dikshanta.HospitalManagement.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppData {
    private Superadmin superadmin;
    private Jwt jwt;

    @Getter
    @Setter
    public static class Superadmin {
        private String name;
        private String username;
        private String password;

    }

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private Long expiry;


    }
}
