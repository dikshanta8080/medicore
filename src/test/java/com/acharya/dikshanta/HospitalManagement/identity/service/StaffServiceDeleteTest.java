package com.acharya.dikshanta.HospitalManagement.identity.service;

import com.acharya.dikshanta.HospitalManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.HospitalManagement.identity.model.Staff;
import com.acharya.dikshanta.HospitalManagement.identity.model.User;
import com.acharya.dikshanta.HospitalManagement.identity.repository.StaffRepository;
import com.acharya.dikshanta.HospitalManagement.identity.repository.UserRepository;
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
class StaffServiceDeleteTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StaffService staffService;

    @Test
    @DisplayName("Should soft delete staff and associated user when staff exists")
    void testDeleteStaff_Success() {
        UUID staffId = UUID.randomUUID();
        User user = User.builder().username("staffuser").build();
        Staff staff = Staff.builder().name("John Staff").user(user).build();

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));

        staffService.deleteStaff(staffId);

        verify(userRepository, times(1)).delete(user);
        verify(staffRepository, times(1)).delete(staff);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent staff")
    void testDeleteStaff_NotFound() {
        UUID staffId = UUID.randomUUID();
        when(staffRepository.findById(staffId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> staffService.deleteStaff(staffId));
    }
}
