package com.wordline.onlinesales.acl;

import com.wordline.onlinesales.enums.ClientType;
import com.wordline.onlinesales.model.CartRequest;
import com.wordline.onlinesales.model.CartResponse;
import com.wordline.onlinesales.service.CartService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartApiAdapter {

    private final CartService cartService;

    public CartApiAdapter(CartService cartService) {
        this.cartService = cartService;
    }

    public CartResponse calculateCart(CartRequest cartRequest) {

        CartRequest.ClientData client =
                Optional.ofNullable(cartRequest)
                        .map(CartRequest::getClient)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Client data is required"));

        Optional.of(client)
                .filter(c -> c.getType() == ClientType.PROFESSIONAL)
                .filter(c -> c.getAnnualRevenue() == null)
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "Annual revenue is required for professional clients");
                });

        return cartService.calculateTotal(cartRequest);
    }
}
