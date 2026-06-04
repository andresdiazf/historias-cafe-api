package com.historias_de_cafe.backend.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponseDto(
        Long id,
        Long productId,
        String productName,
        Integer quantityProducts
) {}