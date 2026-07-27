package com.acharya.dikshanta.HospitalManagement.identity.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import com.acharya.dikshanta.HospitalManagement.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "unq_username", columnNames = {"username"}),
                @UniqueConstraint(name = "unq_email", columnNames = {"email"})})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User extends BaseEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
