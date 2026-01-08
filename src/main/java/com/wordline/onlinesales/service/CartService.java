package com.wordline.onlinesales.service;

import com.wordline.onlinesales.enums.ProductType;
import com.wordline.onlinesales.model.CartRequest;
import com.wordline.onlinesales.model.CartResponse;
import com.wordline.onlinesales.service.factory.ClientFactory;
import com.wordline.onlinesales.service.mapper.CartResponseMapper;
import com.wordline.onlinesales.service.pricing.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CartService {

    private final PricingService pricingService;

    public CartService(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    public CartResponse calculateTotal(CartRequest request) {

        var client = ClientFactory.create(request.getClient());

        var total = request.getItems().entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue() > 0)
                .map(e ->
                        pricingService.getPrice(
                                client,
                                ProductType.valueOf(e.getKey())
                        ).multiply(BigDecimal.valueOf(e.getValue()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return CartResponseMapper.toResponse(client, total);
    }
}
