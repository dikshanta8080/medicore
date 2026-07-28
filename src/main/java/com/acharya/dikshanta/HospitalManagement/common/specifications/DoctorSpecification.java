package com.acharya.dikshanta.HospitalManagement.common.specifications;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.DoctorFilterRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {

    public static Specification<Doctor> filterDoctors(DoctorFilterRequest request) {
        return (root, query, cb) -> {
            if (request == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.name())) {
                String namePattern = "%" + request.name().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("staff").get("name")), namePattern));
            }

            if (request.gender() != null) {
                predicates.add(cb.equal(root.get("staff").get("gender"), request.gender()));
            }

            if (request.departmentId() != null) {
                predicates.add(cb.equal(root.get("department").get("id"), request.departmentId()));
            }

            if (request.specializationId() != null) {
                predicates.add(cb.equal(root.get("specialization").get("id"), request.specializationId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


