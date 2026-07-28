package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.AssignHodRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.RemoveHodRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.ReplaceHodRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DepartmentResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DoctorResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DepartmentMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DoctorMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Doctor;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DepartmentRepository;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        checkIfDepartmentAlreadyExists(request.name());
        Department department = departmentMapper.toEntity(request);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedResponse<DepartmentResponse> getDepartments(Pageable pageable) {
        Page<DepartmentResponse> responsePage = departmentRepository.findAll(pageable)
                .map(departmentMapper::toResponse);
        return PagedResponse.toPagedResponse(responsePage);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(UUID id) {
        Department department = findDepartment(id);
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public DepartmentResponse updateDepartment(UpdateDepartmentRequest request) {
        Department department = findDepartment(request.departmentId());
        if (request.name() != null && !request.name().equals(department.getName())) {
            checkIfDepartmentAlreadyExists(request.name());
            department.setName(request.name());
        }
        if (request.description() != null) {
            department.setDescription(request.description());
        }
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public DoctorResponse assignHod(AssignHodRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        if (doctor.getDepartment() == null || !doctor.getDepartment().getId().equals(department.getId())) {
            throw new BusinessException("Doctor must be from same department");
        }
        department.setHod(doctor);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public DoctorResponse replaceHod(ReplaceHodRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        if (department.getHod() == null) {
            throw new BusinessException("Department has no current HOD. Use assignHod instead");
        }
        Doctor newHod = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        if (newHod.getDepartment() == null || !newHod.getDepartment().getId().equals(department.getId())) {
            throw new BusinessException("Doctor must be from the same department");
        }
        if (department.getHod().getId().equals(newHod.getId())) {
            throw new BusinessException("Doctor is already the HOD of this department");
        }
        department.setHod(newHod);
        return doctorMapper.toResponse(newHod);
    }

    @Transactional
    public DepartmentResponse removeHod(RemoveHodRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        if (department.getHod() == null) {
            throw new BusinessException("Department already has no HOD");
        }
        department.setHod(null);
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = findDepartment(id);
        departmentRepository.delete(department);
    }

    private Department findDepartment(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    private void checkIfDepartmentAlreadyExists(String name) {
        if (departmentRepository.existsByName(name)) {
            throw new BusinessException("Department already exists");
        }
    }
}
