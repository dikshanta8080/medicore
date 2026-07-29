package com.acharya.dikshanta.HospitalManagement.doctor.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DoctorSchedule extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Days dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
