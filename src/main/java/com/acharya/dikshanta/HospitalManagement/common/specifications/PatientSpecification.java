    package com.acharya.dikshanta.HospitalManagement.common.specifications;

    import com.acharya.dikshanta.HospitalManagement.common.enums.BloodGroup;
    import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
    import com.acharya.dikshanta.HospitalManagement.patient.model.Patient;
    import org.springframework.data.jpa.domain.Specification;

    import java.util.ArrayList;
    import java.util.List;
    import jakarta.persistence.criteria.Predicate;

    public class PatientSpecification {

        public static Specification<Patient> search(
                String patientNumber,
                String fullName,
                String phoneNumber,
                Gender gender,
                BloodGroup bloodGroup
        ) {

            return (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (patientNumber != null && !patientNumber.isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("patientNumber")),
                            "%" + patientNumber.toLowerCase() + "%"
                    ));
                }

                if (fullName != null && !fullName.isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("fullName")),
                            "%" + fullName.toLowerCase() + "%"
                    ));
                }

                if (phoneNumber != null && !phoneNumber.isBlank()) {
                    predicates.add(cb.like(
                            root.get("phoneNumber"),
                            "%" + phoneNumber + "%"
                    ));
                }

                if (gender != null) {
                    predicates.add(cb.equal(root.get("gender"), gender));
                }

                if (bloodGroup != null) {
                    predicates.add(cb.equal(root.get("bloodGroup"), bloodGroup));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
