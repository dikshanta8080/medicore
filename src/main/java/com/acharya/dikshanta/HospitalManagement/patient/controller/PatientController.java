package com.acharya.dikshanta.HospitalManagement.patient.controller;

import com.acharya.dikshanta.HospitalManagement.common.dto.ApiResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.dto.PaginationRequest;
import com.acharya.dikshanta.HospitalManagement.common.enums.BloodGroup;
import com.acharya.dikshanta.HospitalManagement.common.enums.Gender;
import com.acharya.dikshanta.HospitalManagement.patient.dto.request.CreatePatientRequest;
import com.acharya.dikshanta.HospitalManagement.patient.dto.response.PatientResponse;
import com.acharya.dikshanta.HospitalManagement.patient.repository.PatientRepository;
import com.acharya.dikshanta.HospitalManagement.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> create(@RequestBody CreatePatientRequest request){
        PatientResponse patientResponse = patientService.create(request);
        ApiResponse<PatientResponse> apiResponse = ApiResponse.<PatientResponse>builder()
                .data(patientResponse)
                .message("Patient created successfully")
                .status(true)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable UUID id){
        PatientResponse patientResponse = patientService.getPatientById(id);
        ApiResponse<PatientResponse> apiResponse = ApiResponse.<PatientResponse>builder()
                .data(patientResponse)
                .message("Patient fetched successfully")
                .status(true)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<PatientResponse>>> getPatients(
            @ModelAttribute PaginationRequest request,
            @RequestParam(required = false) String patientNumber,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) BloodGroup bloodGroup) {

        PagedResponse<PatientResponse> patientPage = patientService.getAllPatients(
                patientNumber,
                fullName,
                phoneNumber,
                gender,
                bloodGroup,
                request.toPageable()
        );

        ApiResponse<PagedResponse<PatientResponse>> apiResponse =
                ApiResponse.<PagedResponse<PatientResponse>>builder()
                        .data(patientPage)
                        .message("Patients fetched successfully")
                        .status(true)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(@PathVariable UUID id,
                                                                      @RequestBody CreatePatientRequest request ){
        PatientResponse updateResponse = patientService.update(request, id);
        ApiResponse<PatientResponse> apiResponse = ApiResponse.<PatientResponse>builder()
                .data(updateResponse)
                .message("Patient updated successfully")
                .status(true)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully",null));
    }
}
