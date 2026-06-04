package com.historias_de_cafe.backend.security;

import com.historias_de_cafe.backend.model.User;
import com.historias_de_cafe.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("=== DEBUG JWT ===");
        System.out.println("User found: " + user.getEmail());
        System.out.println("User ID: " + user.getId());
        System.out.println("Role object: " + user.getRole());
        System.out.println("Role name: " + user.getRole().name());
        System.out.println("Role toString: " + user.getRole().toString());

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        System.out.println("Authorities generated: " + authorities);
        System.out.println("=== END DEBUG ===");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Boolean.TRUE.equals(user.getStateActive()),
                true,
                true,
                true,
                authorities
        );
    }
}
