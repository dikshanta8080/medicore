package com.acharya.dikshanta.HospitalManagement.doctor.service;

import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.doctor.model.Specialization;
import com.acharya.dikshanta.HospitalManagement.doctor.repository.SpecializationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceDeleteTest {

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private SpecializationService specializationService;

    @Test
    @DisplayName("Should soft delete specialization when it exists")
    void testDeleteSpecialization_Success() {
        UUID specId = UUID.randomUUID();
        Specialization specialization = Specialization.builder().name("Cardiology").build();

        when(specializationRepository.findById(specId)).thenReturn(Optional.of(specialization));

        specializationService.deleteSpecialization(specId);

        verify(specializationRepository, times(1)).delete(specialization);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent specialization")
    void testDeleteSpecialization_NotFound() {
        UUID specId = UUID.randomUUID();
        when(specializationRepository.findById(specId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> specializationService.deleteSpecialization(specId));
    }
}
