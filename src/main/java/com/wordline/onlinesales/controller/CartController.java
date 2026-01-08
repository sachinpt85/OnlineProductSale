package com.wordline.onlinesales.controller;

import com.wordline.onlinesales.acl.CartApiAdapter;
import com.wordline.onlinesales.model.CartRequest;
import com.wordline.onlinesales.model.CartResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartApiAdapter cartApiAdapter;

    public CartController(CartApiAdapter cartApiAdapter) {
        this.cartApiAdapter = cartApiAdapter;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CartResponse> calculateCart(
            @Valid @RequestBody CartRequest request) {

        var response =
                cartApiAdapter.calculateCart(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API is running");
    }
}
