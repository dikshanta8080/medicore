package com.acharya.dikshanta.HospitalManagement.identity.mapper;

import com.acharya.dikshanta.HospitalManagement.identity.dto.request.CreateStaffRequest;
import com.acharya.dikshanta.HospitalManagement.identity.dto.response.StaffResponse;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {
    public Staff toStaff(CreateStaffRequest request) {
        return Staff.builder()
                .name(request.name())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .gender(request.gender())
                .build();
    }

    public StaffResponse toResponse(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .name(staff.getName())
                .email(staff.getUser().getEmail())
                .username(staff.getUser().getUsername())
                .address(staff.getAddress())
                .phoneNumber(staff.getPhoneNumber())
                .gender(staff.getGender())
                .build();
    }


}
