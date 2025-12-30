package com.wordline.onlinesales.controller;


import com.wordline.onlinesales.model.CartRequest;
import com.wordline.onlinesales.model.CartResponse;
import com.wordline.onlinesales.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/calculate")
    public CartResponse calculateCart(@Valid @RequestBody CartRequest request) {
        return cartService.calculateTotal(request);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "API is running";
    }
}
