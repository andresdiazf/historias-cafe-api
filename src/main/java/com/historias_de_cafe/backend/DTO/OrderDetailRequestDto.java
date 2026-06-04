package com.historias_de_cafe.backend.DTO;

public record OrderDetailRequestDto(
        Long productId,
        Integer quantityProducts
) {}
