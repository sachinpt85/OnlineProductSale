package com.wordline.onlinesales.model;

import java.math.BigDecimal;

public record CartResponse(
        String clientId,
        String clientName,
        String clientType,
        BigDecimal total,
        String currency
) {}