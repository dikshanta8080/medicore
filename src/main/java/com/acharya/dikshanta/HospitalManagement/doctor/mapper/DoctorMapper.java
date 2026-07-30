package com.acharya.dikshanta.HospitalManagement.doctor.mapper;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDoctorRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
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

        Staff staff = doctor.getStaff();
        User user = staff != null ? staff.getUser() : null;

        return DoctorResponse.builder()
                .name(staff != null ? staff.getName() : null)
                .email(user != null ? user.getEmail() : null)
                .username(user != null ? user.getUsername() : null)
                .address(staff != null ? staff.getAddress() : null)
                .phoneNumber(staff != null ? staff.getPhoneNumber() : null)
                .gender(staff != null ? staff.getGender() : null)
                .consultationFee(doctor.getConsultationFee())
                .licenseNumber(doctor.getLicenseNumber())
                .specialization(
                        doctor.getSpecialization() != null
                                ? doctor.getSpecialization().getName()
                                : null
                )
                .department(
                        doctor.getDepartment() != null
                                ? doctor.getDepartment().getName()
                                : null
                )
                .build();
    }
}