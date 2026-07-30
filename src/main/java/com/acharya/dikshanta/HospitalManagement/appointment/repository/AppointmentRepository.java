package com.acharya.dikshanta.HospitalManagement.appointment.repository;

import com.acharya.dikshanta.HospitalManagement.appointment.enums.AppointmentStatus;
import com.acharya.dikshanta.HospitalManagement.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByAppointmentDateOrderByAppointmentTimeAsc(LocalDate appointmentDate);

    List<Appointment> findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(
            UUID doctorId, LocalDate appointmentDate);

    @Query("""
            SELECT COUNT(a) > 0 FROM Appointment a
            WHERE a.doctor.id = :doctorId
            AND a.appointmentDate = :appointmentDate
            AND a.appointmentTime = :appointmentTime
            AND (:excludeId IS NULL OR a.id <> :excludeId)
            AND a.appointmentStatus NOT IN :excludedStatuses
            """)
    boolean existsConflictingAppointment(
            @Param("doctorId") UUID doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("appointmentTime") LocalTime appointmentTime,
            @Param("excludeId") UUID excludeId,
            @Param("excludedStatuses") Collection<AppointmentStatus> excludedStatuses);
}
