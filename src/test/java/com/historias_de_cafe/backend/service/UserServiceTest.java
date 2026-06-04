package com.historias_de_cafe.backend.service;

import com.historias_de_cafe.backend.model.Role;
import com.historias_de_cafe.backend.model.User;
import com.historias_de_cafe.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAllUserReturnsUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user()));

        List<User> response = userService.findAllUser();

        assertEquals(1, response.size());
        assertEquals("Ana Perez", response.get(0).getName());
    }

    @Test
    void createClearsIdSetsCreationDateAndDefaultActiveState() {
        User user = user();
        user.setId(99L);
        user.setCreationDate(null);
        user.setStateActive(null);

        when(userRepository.save(user)).thenReturn(user);

        User response = userService.create(user);

        assertNull(response.getId());
        assertNotNull(response.getCreationDate());
        assertTrue(response.getStateActive());
        verify(userRepository).save(user);
    }

    @Test
    void getByIdReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));

        User response = userService.getById(1L);

        assertEquals("ana@example.com", response.getEmail());
    }

    @Test
    void updateChangesExistingUser() {
        User existing = user();
        User request = new User(null, "Carlos", "carlos@example.com", "new-password", Role.ADMIN, null, false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User response = userService.update(1L, request);

        assertEquals("Carlos", response.getName());
        assertEquals("carlos@example.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
        assertEquals(false, response.getStateActive());
    }

    @Test
    void patchOnlyChangesProvidedFields() {
        User existing = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User response = userService.patch(1L, Map.of("name", "Ana Maria", "stateActive", false));

        assertEquals("Ana Maria", response.getName());
        assertEquals("ana@example.com", response.getEmail());
        assertEquals(false, response.getStateActive());
    }

    @Test
    void deleteRemovesExistingUser() {
        User existing = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        userService.delete(1L);

        verify(userRepository).delete(existing);
    }

    @Test
    void getByIdThrowsWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getById(99L));
    }

    private User user() {
        return new User(1L, "Ana Perez", "ana@example.com", "password123", Role.CLIENT, LocalDateTime.now(), true);
    }
}
