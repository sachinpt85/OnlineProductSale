package com.wordline.onlinesales.service;

import com.wordline.onlinesales.enums.ClientType;
import com.wordline.onlinesales.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
    }

    // Test 1: Individual Client - Basic Calculation
    @Test
    void testCalculateIndividualClientCart() {
        CartRequest request = createIndividualRequest(
                "IND001", "John", "Doe",
                Map.of("HIGH_END_PHONE", 2, "LAPTOP", 1)
        );

        CartResponse response = cartService.calculateTotal(request);

        assertEquals("IND001", response.clientId());
        assertEquals("John Doe", response.clientName());
        assertEquals("INDIVIDUAL", response.clientType());
        assertEquals(new BigDecimal("4200.00"), response.total());
        assertEquals("EUR", response.currency());
    }

    // Test 2: Professional High Revenue Client
    @Test
    void testCalculateProfessionalHighRevenueClient() {
        CartRequest request = createProfessionalRequest(
                "PROF001", "TechCorp", 15000000.0, "BRN001", "VAT001",
                Map.of("HIGH_END_PHONE", 5, "MID_RANGE_PHONE", 10, "LAPTOP", 3)
        );

        CartResponse response = cartService.calculateTotal(request);

        assertEquals("PROF001", response.clientId());
        assertEquals("TechCorp", response.clientName());
        assertEquals("PROFESSIONAL", response.clientType());
        assertEquals(new BigDecimal("13200.00"), response.total()); // 5*1000 + 10*550 + 3*900
        assertEquals("EUR", response.currency());
    }

    // Test 3: Professional Low Revenue Client
    @Test
    void testCalculateProfessionalLowRevenueClient() {
        CartRequest request = createProfessionalRequest(
                "PROF002", "SmallBiz", 5000000.0, "BRN002", null,
                Map.of("HIGH_END_PHONE", 3, "LAPTOP", 2)
        );

        CartResponse response = cartService.calculateTotal(request);

        assertEquals("PROF002", response.clientId());
        assertEquals("SmallBiz", response.clientName());
        assertEquals("PROFESSIONAL", response.clientType());
        assertEquals(new BigDecimal("5450.00"), response.total()); // 3*1150 + 2*1000
        assertEquals("EUR", response.currency());
    }

    // Test 4: Empty Cart
    @Test
    void testEmptyCart() {
        CartRequest request = createIndividualRequest(
                "IND002", "Jane", "Smith", Map.of()
        );

        CartResponse response = cartService.calculateTotal(request);

        assertEquals(new BigDecimal("0.00"), response.total());
    //    assertEquals(0, response.items() != null ? response.items().size() : 0);
    }

    // Test 5: Revenue Threshold Boundary Tests
    @Test
    void testRevenueThresholdBoundary() {
        // At threshold (10M exactly) - should use low revenue prices
        CartRequest atThreshold = createProfessionalRequest(
                "PROF003", "AtThreshold", 10000000.0, "BRN003", null,
                Map.of("HIGH_END_PHONE", 1)
        );
        CartResponse response1 = cartService.calculateTotal(atThreshold);
        assertEquals(new BigDecimal("1150.00"), response1.total());

        // Just above threshold (10M + 1) - should use high revenue prices
        CartRequest aboveThreshold = createProfessionalRequest(
                "PROF004", "AboveThreshold", 10000001.0, "BRN004", null,
                Map.of("HIGH_END_PHONE", 1)
        );
        CartResponse response2 = cartService.calculateTotal(aboveThreshold);
        assertEquals(new BigDecimal("1000.00"), response2.total());

        // Just below threshold (10M - 1)
        CartRequest belowThreshold = createProfessionalRequest(
                "PROF005", "BelowThreshold", 9999999.0, "BRN005", null,
                Map.of("HIGH_END_PHONE", 1)
        );
        CartResponse response3 = cartService.calculateTotal(belowThreshold);
        assertEquals(new BigDecimal("1150.00"), response3.total());
    }

    // Test 6: All Product Types for Individual Client
    @Test
    void testAllProductTypesIndividual() {
        CartRequest request = createIndividualRequest(
                "IND003", "Alice", "Johnson",
                Map.of("HIGH_END_PHONE", 1, "MID_RANGE_PHONE", 1, "LAPTOP", 1)
        );

        CartResponse response = cartService.calculateTotal(request);
        assertEquals(new BigDecimal("3500.00"), response.total()); // 1500 + 800 + 1200
    }

    // Test 7: All Product Types for Professional High Revenue
    @Test
    void testAllProductTypesProfessionalHighRevenue() {
        CartRequest request = createProfessionalRequest(
                "PROF006", "BigCorp", 20000000.0, "BRN006", "VAT006",
                Map.of("HIGH_END_PHONE", 1, "MID_RANGE_PHONE", 1, "LAPTOP", 1)
        );

        CartResponse response = cartService.calculateTotal(request);
        assertEquals(new BigDecimal("2450.00"), response.total()); // 1000 + 550 + 900
    }

    // Test 8: All Product Types for Professional Low Revenue
    @Test
    void testAllProductTypesProfessionalLowRevenue() {
        CartRequest request = createProfessionalRequest(
                "PROF007", "MediumCorp", 5000000.0, "BRN007", null,
                Map.of("HIGH_END_PHONE", 1, "MID_RANGE_PHONE", 1, "LAPTOP", 1)
        );

        CartResponse response = cartService.calculateTotal(request);
        assertEquals(new BigDecimal("2750.00"), response.total()); // 1150 + 600 + 1000
    }

    // Test 9: Zero Quantity Items (should be ignored)
    @Test
    void testZeroQuantityItems() {
        CartRequest request = createIndividualRequest(
                "IND004", "Bob", "Brown",
                Map.of("HIGH_END_PHONE", 0, "LAPTOP", 2, "MID_RANGE_PHONE", 0)
        );

        CartResponse response = cartService.calculateTotal(request);
        assertEquals(new BigDecimal("2400.00"), response.total()); // Only 2 laptops = 2*1200
    }

    // Test 10: Case Insensitive Product Types (should work)
    @Test
    void testCaseInsensitiveProductTypes() {
        CartRequest request = createIndividualRequest(
                "IND005", "Charlie", "Davis",
                Map.of("high_end_phone", 1, "Mid_Range_Phone", 1, "LAPTOP", 1)
        );

        CartResponse response = cartService.calculateTotal(request);
        assertEquals(new BigDecimal("3500.00"), response.total());
    }

    @Test
    void testNullClientType() {
        // Test that service handles null client type
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();
       // clientData.setType("null");
        clientData.setFirstName("Test");
        clientData.setLastName("User");
        request.setClient(clientData);
        request.setItems(Map.of("HIGH_END_PHONE", 1));

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.calculateTotal(request);
        });
    }
    // Test 12: Missing Required Fields for Individual
    @Test
    void testMissingIndividualFields() {
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();
        clientData.setType("INDIVIDUAL");
        clientData.setClientId("IND006");
        // Missing firstName and lastName
        request.setClient(clientData);
        request.setItems(Map.of("HIGH_END_PHONE", 1));

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.calculateTotal(request);
        });
    }

    // Test 13: Missing Required Fields for Professional
    @Test
    void testMissingProfessionalFields() {
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();
        clientData.setType("PROFESSIONAL");
        clientData.setClientId("PROF008");
        clientData.setCompanyName("TestCorp");
        // Missing annualRevenue and businessRegistrationNumber
        request.setClient(clientData);
        request.setItems(Map.of("HIGH_END_PHONE", 1));

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.calculateTotal(request);
        });
    }

    // Test 14: Null Request
    @Test
    void testNullRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.calculateTotal(null);
        });
    }

    // Test 15: Null Client Data
    @Test
    void testNullClientData() {
        CartRequest request = new CartRequest();
        request.setItems(Map.of("HIGH_END_PHONE", 1));

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.calculateTotal(request);
        });
    }

    // Test 16: Large Quantities
    @Test
    void testLargeQuantities() {
        CartRequest request = createProfessionalRequest(
                "PROF009", "Enterprise", 50000000.0, "BRN009", "VAT009",
                Map.of("HIGH_END_PHONE", 100, "MID_RANGE_PHONE", 200, "LAPTOP", 50)
        );

        CartResponse response = cartService.calculateTotal(request);

        BigDecimal expected = new BigDecimal("1000.00")  // High-end phone
                .multiply(new BigDecimal("100"))
                .add(new BigDecimal("550.00")   // Mid-range phone
                        .multiply(new BigDecimal("200")))
                .add(new BigDecimal("900.00")   // Laptop
                        .multiply(new BigDecimal("50")));

        assertEquals(expected, response.total());
    }

    // Test 17: Mixed Case Client Type in JSON
    @Test
    void testMixedCaseClientType() {
        // This tests that Jackson can handle mixed case
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData(); // No-arg constructor
        clientData.setType("individual"); // Use setter
        clientData.setClientId("IND007");
        clientData.setFirstName("David");
        clientData.setLastName("Wilson");
        request.setClient(clientData);
        request.setItems(Map.of("LAPTOP", 1));

        CartResponse response = cartService.calculateTotal(request);

        assertEquals("IND007", response.clientId());
        assertEquals("David Wilson", response.clientName());
        assertEquals("INDIVIDUAL", response.clientType());
        assertEquals(new BigDecimal("1200.00"), response.total());
    }

    // Test 18: Professional without VAT Number (optional)
    @Test
    void testProfessionalWithoutVatNumber() {
        CartRequest request = createProfessionalRequest(
                "PROF010", "NoVATCorp", 8000000.0, "BRN010", null,
                Map.of("MID_RANGE_PHONE", 5)
        );

        CartResponse response = cartService.calculateTotal(request);

        assertEquals("PROF010", response.clientId());
        assertEquals("NoVATCorp", response.clientName());
        assertEquals("PROFESSIONAL", response.clientType());
        assertEquals(new BigDecimal("3000.00"), response.total()); // 5 * 600
    }

    // Test 19: Decimal Quantities (not applicable, but good to test)
    @Test
    void testInvalidProductType() {
        CartRequest request = createIndividualRequest(
                "IND008", "Eve", "Taylor",
                Map.of("INVALID_PRODUCT", 1, "LAPTOP", 1)
        );

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.calculateTotal(request);
        });
    }

    // Test 20: Null Product Quantity
    @Test
    void testNullQuantity() {
        Map<String, Integer> items = new HashMap<>();
        items.put("LAPTOP", null);
        items.put("HIGH_END_PHONE", 2);

        CartRequest request = createIndividualRequest(
                "IND009", "Frank", "Moore", items
        );

        CartResponse response = cartService.calculateTotal(request);
        // Should only count HIGH_END_PHONE: 2 * 1500 = 3000
        assertEquals(new BigDecimal("3000.00"), response.total());
    }

    // Helper methods
    private CartRequest createIndividualRequest(String clientId, String firstName,
                                                String lastName, Map<String, Integer> items) {
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();
        clientData.setType("INDIVIDUAL");
        clientData.setClientId(clientId);
        clientData.setFirstName(firstName);
        clientData.setLastName(lastName);
        request.setClient(clientData);
        request.setItems(items);
        return request;
    }

    private CartRequest createProfessionalRequest(String clientId, String companyName,
                                                  double annualRevenue, String businessRegNumber,
                                                  String vatNumber, Map<String, Integer> items) {
        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData(); // No-arg constructor
        clientData.setType("PROFESSIONAL"); // Use setter
        clientData.setClientId(clientId);
        clientData.setCompanyName(companyName);
        clientData.setAnnualRevenue(annualRevenue);
        clientData.setBusinessRegistrationNumber(businessRegNumber);
        clientData.setIntraCommunityVatNumber(vatNumber);
        request.setClient(clientData);
        request.setItems(items);
        return request;
    }
}