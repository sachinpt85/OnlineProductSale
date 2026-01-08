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

    @Test
    void calculateCart_EndToEnd_Success() throws Exception {

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
}
