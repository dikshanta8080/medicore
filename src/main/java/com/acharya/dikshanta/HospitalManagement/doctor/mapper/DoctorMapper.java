package com.acharya.dikshanta.HospitalManagement.doctor.mapper;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public Doctor toEntity(CreateDoctorRequest request) {
        return Doctor.builder()
                .consultationFee(request.consultationFee())
                .licenseNumber(request.licenseNumber())
                .build();
    }

    public DoctorResponse toResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .name(doctor.getStaff().getName())
                .email(doctor.getStaff().getUser().getEmail())
                .username(doctor.getStaff().getUser().getUsername())
                .address(doctor.getStaff().getAddress())
                .phoneNumber(doctor.getStaff().getPhoneNumber())
                .gender(doctor.getStaff().getGender())
                .consultationFee(doctor.getConsultationFee())
                .licenseNumber(doctor.getLicenseNumber())
                .specialization(doctor.getSpecialization().getName())
                .department(doctor.getDepartment().getName())
                .build();
    }
}
