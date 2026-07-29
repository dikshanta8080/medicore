package com.acharya.dikshanta.HospitalManagement.common.specifications;

import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.FilterDoctorScheduleRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.model.DoctorSchedule;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DoctorScheduleSpecification {

    public static Specification<DoctorSchedule> filterSchedules(FilterDoctorScheduleRequest request) {
        return (root, query, cb) -> {
            if (request == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (request.day() != null) {
                predicates.add(cb.equal(root.get("dayOfWeek"), request.day()));
            }

            if (request.doctorId() != null) {
                predicates.add(cb.equal(root.get("doctor").get("id"), request.doctorId()));
            }

            if (request.departmentId() != null) {
                predicates.add(cb.equal(root.get("doctor").get("department").get("id"), request.departmentId()));
            }

            if (request.specializationId() != null) {
                predicates.add(cb.equal(root.get("doctor").get("specialization").get("id"), request.specializationId()));
            }

            if (StringUtils.hasText(request.doctorName())) {
                String namePattern = "%" + request.doctorName().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("doctor").get("staff").get("name")), namePattern));
            }

            if (request.availableAt() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), request.availableAt()));
                predicates.add(cb.greaterThan(root.get("endTime"), request.availableAt()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
