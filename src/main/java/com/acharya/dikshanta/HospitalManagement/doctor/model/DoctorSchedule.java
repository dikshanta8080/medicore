package com.acharya.dikshanta.HospitalManagement.doctor.model;

import com.acharya.dikshanta.HospitalManagement.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "doctor_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DoctorSchedule extends BaseEntity {
    private String dayOfWeek;
}
