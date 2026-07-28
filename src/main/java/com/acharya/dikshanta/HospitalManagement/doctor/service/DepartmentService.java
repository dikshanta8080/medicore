package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.dto.PagedResponse;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.CreateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.request.UpdateDepartmentRequest;
import com.acharya.dikshanta.HospitalManagement.doctor.dto.response.DepartmentResponse;
import com.acharya.dikshanta.HospitalManagement.doctor.mapper.DepartmentMapper;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Department;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.DepartmentRepository;
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
