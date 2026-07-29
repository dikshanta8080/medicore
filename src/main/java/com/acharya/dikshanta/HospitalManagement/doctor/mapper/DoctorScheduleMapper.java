package com.acharya.dikshanta.HospitalManagement.doctor.mapper;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorScheduleResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import org.springframework.stereotype.Component;

@Component
public class DoctorScheduleMapper {

    public DoctorScheduleResponse toResponse(DoctorSchedule schedule) {
        Doctor doctor = schedule.getDoctor();
        return DoctorScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(doctor.getId())
                .doctorName(doctor.getStaff().getName())
                .department(doctor.getDepartment().getName())
                .specialization(doctor.getSpecialization().getName())
                .day(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
