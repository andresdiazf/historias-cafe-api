package com.historias_de_cafe.backend.DTO;

import java.util.List;

public record OrderRequestDto(
        Long userId,
        String stateOrder,
        List<OrderDetailRequestDto> details
) {}