package com.historias_de_cafe.backend.DTO;

public record CategoriesResponseDTO(
        Integer id,
        String toastingType,
        String regionOrigin,
        String presentation
) {
}
