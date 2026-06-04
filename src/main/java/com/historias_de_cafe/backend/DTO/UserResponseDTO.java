package com.historias_de_cafe.backend.DTO;
import com.historias_de_cafe.backend.model.User;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;

    public UserResponseDTO() {
    }

    public static UserResponseDTO from(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.name = user.getName();
        dto.role = user.getRole() != null ? user.getRole().toValue() : null;
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
