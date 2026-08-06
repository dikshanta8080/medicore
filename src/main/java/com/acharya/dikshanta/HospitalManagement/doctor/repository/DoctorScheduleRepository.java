package com.acharya.dikshanta.HospitalManagement.doctor.repository;

import com.acharya.dikshanta.HospitalManagement.common.enums.Days;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID>, JpaSpecificationExecutor<DoctorSchedule> {

    @Query("""
            SELECT COUNT(s) > 0 FROM DoctorSchedule s
            WHERE s.doctor.id = :doctorId
            AND s.dayOfWeek = :day
            AND (:excludeId IS NULL OR s.id <> :excludeId)
            AND s.startTime < :endTime
            AND s.endTime > :startTime
            """)
    boolean existsOverlappingSchedule(
            @Param("doctorId") UUID doctorId,
            @Param("day") Days day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") UUID excludeId
    );

    boolean existsByDoctorId(UUID doctorId);

    @Query("""
            SELECT COUNT(s) > 0 FROM DoctorSchedule s
            WHERE s.doctor.id = :doctorId
            AND s.dayOfWeek = :day
            AND s.startTime <= :time
            AND s.endTime > :time
            """)
    boolean isDoctorAvailableAt(
            @Param("doctorId") UUID doctorId,
            @Param("day") Days day,
            @Param("time") LocalTime time
    );
}
