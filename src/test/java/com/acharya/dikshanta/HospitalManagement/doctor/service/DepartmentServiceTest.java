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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    @DisplayName("Should create department successfully")
    void testCreateDepartment_Success() {
        CreateDepartmentRequest request = new CreateDepartmentRequest("Cardiology", "Heart department");
        Department department = Department.builder().name("Cardiology").description("Heart department").build();
        DepartmentResponse response = DepartmentResponse.builder().id(UUID.randomUUID()).name("Cardiology").description("Heart department").build();

        when(departmentRepository.existsByName("Cardiology")).thenReturn(false);
        when(departmentMapper.toEntity(request)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.createDepartment(request);

        assertNotNull(result);
        assertEquals("Cardiology", result.name());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    @DisplayName("Should throw BusinessException when creating department that already exists")
    void testCreateDepartment_AlreadyExists() {
        CreateDepartmentRequest request = new CreateDepartmentRequest("Cardiology", "Heart department");
        when(departmentRepository.existsByName("Cardiology")).thenReturn(true);

        assertThrows(BusinessException.class, () -> departmentService.createDepartment(request));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fetch department by ID")
    void testGetDepartment_Success() {
        UUID id = UUID.randomUUID();
        Department department = Department.builder().name("Neurology").description("Brain department").build();
        DepartmentResponse response = DepartmentResponse.builder().id(id).name("Neurology").description("Brain department").build();

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.getDepartment(id);

        assertNotNull(result);
        assertEquals("Neurology", result.name());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when department is not found")
    void testGetDepartment_NotFound() {
        UUID id = UUID.randomUUID();
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartment(id));
    }

    @Test
    @DisplayName("Should update department successfully")
    void testUpdateDepartment_Success() {
        UUID id = UUID.randomUUID();
        UpdateDepartmentRequest request = new UpdateDepartmentRequest(id, "Cardiology Updated", "New description");
        Department department = Department.builder().name("Cardiology").description("Old description").build();
        DepartmentResponse response = DepartmentResponse.builder().id(id).name("Cardiology Updated").description("New description").build();

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
        when(departmentRepository.existsByName("Cardiology Updated")).thenReturn(false);
        when(departmentMapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.updateDepartment(request);

        assertNotNull(result);
        assertEquals("Cardiology Updated", result.name());
    }

    @Test
    @DisplayName("Should soft delete department successfully")
    void testDeleteDepartment_Success() {
        UUID id = UUID.randomUUID();
        Department department = Department.builder().name("Orthopedics").description("Bones").build();

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(id);

        verify(departmentRepository, times(1)).delete(department);
    }
}
