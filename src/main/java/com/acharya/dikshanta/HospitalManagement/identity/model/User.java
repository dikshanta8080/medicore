package com.acharya.dikshanta.HospitalManagement.identity.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import com.acharya.dikshanta.HospitalManagement.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "unq_username", columnNames = {"username"}),
                @UniqueConstraint(name = "unq_email", columnNames = {"email"})})
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class User extends SoftDeleteEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Staff staff;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Patient patient;
}
