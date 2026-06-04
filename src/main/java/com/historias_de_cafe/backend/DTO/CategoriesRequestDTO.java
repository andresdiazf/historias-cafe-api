package com.historias_de_cafe.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriesRequestDTO(
        @NotBlank(message = "El tipo de tostion es obligatorio")
        @Size(max = 100, message = "El tipo de tostion es demasiado largo")
        String toastingType,

        @NotBlank(message = "La region de origen es obligatoria")
        @Size(max = 100, message = "La region de origen es demasiado larga")
        String regionOrigin,

        @NotBlank(message = "La presentacion es obligatoria")
        @Size(max = 100, message = "La presentacion es demasiado larga")
        String presentation
) {
}
