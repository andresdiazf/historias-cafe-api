package com.historias_de_cafe.backend.service;

import com.historias_de_cafe.backend.DTO.AuthRequestDTO;
import com.historias_de_cafe.backend.DTO.AuthResponseDTO;
import com.historias_de_cafe.backend.DTO.RegisterRequestDTO;
import com.historias_de_cafe.backend.DTO.UserResponseDTO;
import com.historias_de_cafe.backend.model.Role;
import com.historias_de_cafe.backend.model.User;
import com.historias_de_cafe.backend.repository.UserRepository;
import com.historias_de_cafe.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerCreatesClientWithNormalizedEmailAndEncodedPassword() {
        RegisterRequestDTO request = registerRequest("  Ana Perez  ", " ANA@EXAMPLE.COM ", "password123");

        when(userRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponseDTO response = authService.register(request);

        assertEquals(1L, response.getId());
        assertEquals("Ana Perez", response.getName());
        assertEquals("ana@example.com", response.getEmail());
        assertEquals("CLIENT", response.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerThrowsWhenEmailAlreadyExists() {
        RegisterRequestDTO request = registerRequest("Ana", "ana@example.com", "password123");
        when(userRepository.existsByEmail("ana@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginAuthenticatesAndReturnsTokenWithUser() {
        AuthRequestDTO request = authRequest(" ANA@EXAMPLE.COM ", "password123");
        User user = user();

        when(userRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(user));
        when(userDetailsService.loadUserByUsername("ana@example.com")).thenReturn(userDetails);
        when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("jwt-token");

        AuthResponseDTO response = authService.login(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("ana@example.com", response.getUser().getEmail());
        assertNotNull(response.getUser());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginThrowsWhenAuthenticatedEmailDoesNotExist() {
        AuthRequestDTO request = authRequest("missing@example.com", "password123");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    private RegisterRequestDTO registerRequest(String name, String email, String password) {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    private AuthRequestDTO authRequest(String email, String password) {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    private User user() {
        return new User(1L, "Ana Perez", "ana@example.com", "encoded-password", Role.CLIENT, LocalDateTime.now(), true);
    }
}
