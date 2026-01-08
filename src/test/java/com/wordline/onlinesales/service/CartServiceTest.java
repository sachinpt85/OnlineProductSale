package com.wordline.onlinesales.service;

import com.wordline.onlinesales.enums.ClientType;
import com.wordline.onlinesales.enums.ProductType;
import com.wordline.onlinesales.model.CartRequest;
import com.wordline.onlinesales.model.CartResponse;
import com.wordline.onlinesales.service.pricing.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CartServiceTest {

    private CartService cartService;
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = Mockito.mock(PricingService.class);
        cartService = new CartService(pricingService);
    }

    @Test
    void calculateTotal_IndividualClient_Success() {
        CartRequest request = createIndividualRequest(
                Map.of("HIGH_END_PHONE", 2, "LAPTOP", 1)
        );

        when(pricingService.getPrice(any(), eq(ProductType.HIGH_END_PHONE)))
                .thenReturn(new BigDecimal("1500"));
        when(pricingService.getPrice(any(), eq(ProductType.LAPTOP)))
                .thenReturn(new BigDecimal("1200"));

        CartResponse response = cartService.calculateTotal(request);

        assertEquals(new BigDecimal("4200.00"), response.total());
        assertEquals("INDIVIDUAL", response.clientType());
        assertEquals("John Doe", response.clientName());
    }

    @Test
    void calculateTotal_ProfessionalClient_Success() {
        CartRequest request = createProfessionalRequest(
                15_000_000.0,
                Map.of("MID_RANGE_PHONE", 10)
        );

        when(pricingService.getPrice(any(), eq(ProductType.MID_RANGE_PHONE)))
                .thenReturn(new BigDecimal("550"));

        CartResponse response = cartService.calculateTotal(request);

        assertEquals(new BigDecimal("5500.00"), response.total());
        assertEquals("PROFESSIONAL", response.clientType());
        assertEquals("TechCorp", response.clientName());
    }

    @Test
    void calculateTotal_IgnoresZeroAndNullQuantities() {
        Map<String, Integer> items = new HashMap<>();
        items.put("HIGH_END_PHONE", 0);
        items.put("LAPTOP", null);
        items.put("MID_RANGE_PHONE", 2);

        CartRequest request = createIndividualRequest(items);

        when(pricingService.getPrice(any(), eq(ProductType.MID_RANGE_PHONE)))
                .thenReturn(new BigDecimal("800"));

        CartResponse response = cartService.calculateTotal(request);

        assertEquals(new BigDecimal("1600.00"), response.total());
    }

    @Test
    void calculateTotal_InvalidProductType_ShouldThrowException() {
        CartRequest request = createIndividualRequest(
                Map.of("INVALID_PRODUCT", 1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                cartService.calculateTotal(request)
        );
    }

    @Test
    void calculateTotal_NullRequest_ShouldThrowException() {
        assertThrows(NullPointerException.class, () ->
                cartService.calculateTotal(null)
        );
    }

    @Test
    void calculateTotal_NullClient_ShouldThrowException() {
        CartRequest request = new CartRequest();
        request.setItems(Map.of("LAPTOP", 1));

        assertThrows(IllegalArgumentException.class, () ->
                cartService.calculateTotal(request)
        );
    }

    @Test
    void calculateTotal_EmptyItems_ShouldReturnZero() {
        CartRequest request = createIndividualRequest(Map.of());

        CartResponse response = cartService.calculateTotal(request);

        assertEquals(new BigDecimal("0.00"), response.total());
    }

    // -------- Helper methods --------

    private CartRequest createIndividualRequest(Map<String, Integer> items) {
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();

        clientData.setType(ClientType.INDIVIDUAL);
        clientData.setClientId("IND001");
        clientData.setFirstName("John");
        clientData.setLastName("Doe");

        request.setClient(clientData);
        request.setItems(items);
        return request;
    }

    private CartRequest createProfessionalRequest(
            double annualRevenue,
            Map<String, Integer> items) {

        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();

        clientData.setType(ClientType.PROFESSIONAL);
        clientData.setClientId("PROF001");
        clientData.setCompanyName("TechCorp");
        clientData.setAnnualRevenue(annualRevenue);
        clientData.setBusinessRegistrationNumber("BRN001");

        request.setClient(clientData);
        request.setItems(items);
        return request;
    }
}
