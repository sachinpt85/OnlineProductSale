package com.wordline.onlinesales.service.mapper;

import com.wordline.onlinesales.model.CartResponse;
import com.wordline.onlinesales.model.Client;

import java.math.BigDecimal;

public class CartResponseMapper {

    public static CartResponse toResponse(Client client, BigDecimal total) {

        return new CartResponse(
                client.getClientId(),
                client.getDisplayName(),
                client.getClientType().name(),
                total,
                "EUR"
        );
    }
}
