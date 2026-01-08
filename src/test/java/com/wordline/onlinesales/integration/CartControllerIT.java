package com.wordline.onlinesales.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ---------- INDIVIDUAL CLIENT ----------
    @Test
    void calculateCart_EndToEnd_IndividualClient() throws Exception {

        String requestJson = """
            {
              "client": {
                "type": "INDIVIDUAL",
                "clientId": "IND100",
                "firstName": "John",
                "lastName": "Doe"
              },
              "items": {
                "HIGH_END_PHONE": 1,
                "LAPTOP": 1
              }
            }
            """;

        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2700.00));
    }

    // ---------- PROFESSIONAL HIGH REVENUE ----------
    @Test
    void calculateCart_EndToEnd_ProfessionalHighRevenue() throws Exception {

        String requestJson = """
            {
              "client": {
                "type": "PROFESSIONAL",
                "clientId": "PROF200",
                "companyName": "BigCorp",
                "annualRevenue": 15000000,
                "businessRegistrationNumber": "BRN200"
              },
              "items": {
                "HIGH_END_PHONE": 1,
                "MID_RANGE_PHONE": 1,
                "LAPTOP": 1
              }
            }
            """;

        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientType").value("PROFESSIONAL"))
                .andExpect(jsonPath("$.total").value(2450.00));
    }

    // ---------- PROFESSIONAL LOW REVENUE ----------
    @Test
    void calculateCart_EndToEnd_ProfessionalLowRevenue() throws Exception {

        String requestJson = """
            {
              "client": {
                "type": "PROFESSIONAL",
                "clientId": "PROF201",
                "companyName": "SmallBiz",
                "annualRevenue": 5000000,
                "businessRegistrationNumber": "BRN201"
              },
              "items": {
                "HIGH_END_PHONE": 1,
                "MID_RANGE_PHONE": 1,
                "LAPTOP": 1
              }
            }
            """;

        // 1150 + 600 + 1000 = 2750
        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientType").value("PROFESSIONAL"))
                .andExpect(jsonPath("$.total").value(2750.00));
    }
}
