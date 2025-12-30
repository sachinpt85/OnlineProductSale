package com.wordline.onlinesales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordline.onlinesales.enums.ClientType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartRequest {

    private ClientData client;
    private Map<String, Integer> items;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClientData {
        private ClientType type;
        private String clientId;

        // Individual client fields
        private String firstName;
        private String lastName;

        // Professional client fields
        private String companyName;
        private Double annualRevenue;
        private String businessRegistrationNumber;
        private String intraCommunityVatNumber;

        public void setType(String typeStr) {
            if (typeStr == null || typeStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Client type is required");
            }
            try {
                this.type = ClientType.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid client type: " + typeStr +
                        ". Valid values: INDIVIDUAL, PROFESSIONAL");
            }
        }
    }
}