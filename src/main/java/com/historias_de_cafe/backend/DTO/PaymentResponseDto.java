package com.historias_de_cafe.backend.DTO;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        Long id,
        Long orderId,
        String transactionNumber,
        LocalDateTime transactionDate,
        String transactionStatus,
        String paymentUrl
) {}
