package com.historias_de_cafe.backend.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long userId,
        String stateOrder,
        BigDecimal subtotal,
        BigDecimal total,
        LocalDateTime orderDate,
        List<OrderDetailResponseDto> details
) {}
