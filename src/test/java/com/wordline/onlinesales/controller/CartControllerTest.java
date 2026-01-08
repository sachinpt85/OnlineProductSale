package com.wordline.onlinesales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordline.onlinesales.acl.CartApiAdapter;
import com.wordline.onlinesales.model.CartRequest;
import com.wordline.onlinesales.model.CartResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static com.wordline.onlinesales.enums.ClientType.INDIVIDUAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartApiAdapter cartApiAdapter;

    @Test
    void calculateCart_Success_IndividualClient() throws Exception {

        CartRequest request = createIndividualRequest(
                "IND001", "John", "Doe",
                Map.of("HIGH_END_PHONE", 2, "LAPTOP", 1)
        );

        CartResponse mockResponse = new CartResponse(
                "IND001", "John Doe", "INDIVIDUAL",
                new BigDecimal("4200.00"), "EUR"
        );

        when(cartApiAdapter.calculateCart(any(CartRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("IND001"))
                .andExpect(jsonPath("$.clientName").value("John Doe"))
                .andExpect(jsonPath("$.clientType").value("INDIVIDUAL"))
                .andExpect(jsonPath("$.total").value(4200.00))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void calculateCart_Success_ProfessionalClient() throws Exception {

        String requestJson = """
            {
                "client": {
                    "type": "PROFESSIONAL",
                    "clientId": "PROF001",
                    "companyName": "TechCorp",
                    "annualRevenue": 15000000,
                    "businessRegistrationNumber": "BRN001"
                },
                "items": {
                    "HIGH_END_PHONE": 5,
                    "MID_RANGE_PHONE": 10
                }
            }
            """;

        CartResponse mockResponse = new CartResponse(
                "PROF001", "TechCorp", "PROFESSIONAL",
                new BigDecimal("11500.00"), "EUR"
        );

        when(cartApiAdapter.calculateCart(any(CartRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(11500.00));
    }

    @Test
    void calculateCart_InvalidRequest_ReturnsBadRequest() throws Exception {

        String invalidJson = """
            {
                "client": {
                    "type": "INDIVIDUAL",
                    "clientId": "IND001"
                },
                "items": {
                    "HIGH_END_PHONE": 1
                }
            }
            """;

        when(cartApiAdapter.calculateCart(any(CartRequest.class)))
                .thenThrow(new IllegalArgumentException(
                        "First name and last name are required"));

        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void healthCheck_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/cart/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("API is running"));
    }

    private CartRequest createIndividualRequest(
            String clientId,
            String firstName,
            String lastName,
            Map<String, Integer> items) {

        CartRequest request = new CartRequest();
        CartRequest.ClientData clientData = new CartRequest.ClientData();

        clientData.setType(INDIVIDUAL);
        clientData.setClientId(clientId);
        clientData.setFirstName(firstName);
        clientData.setLastName(lastName);

        request.setClient(clientData);
        request.setItems(items);

        return request;
    }
}
