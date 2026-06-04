package com.historias_de_cafe.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN,
    CLIENT;

    @JsonCreator
    public static Role fromValue(String value) {
        if (value == null) {
            return null;
        }

        return switch (value.trim().toUpperCase()) {
            case "ADMIN" -> ADMIN;
            case "CLIENT", "CLIENTE" -> CLIENT;
            default -> throw new IllegalArgumentException("Rol no válido: " + value);
        };
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
