package com.historias_de_cafe.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Roast {
    LIGHT,
    MEDIUM,
    DARK;

    @JsonCreator
    public static Roast fromValue(String value) {
        if (value == null) {
            return null;
        }

        return switch (value.trim().toUpperCase()) {
            case "LIGHT", "TOSTADO_CLARO", "CLARO" -> LIGHT;
            case "MEDIUM", "TOSTADO_MEDIO", "MEDIO" -> MEDIUM;
            case "DARK", "TOSTADO_OSCURO", "OSCURO" -> DARK;
            default -> throw new IllegalArgumentException("Tipo de tostado no válido: " + value);
        };
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
