package com.wordline.onlinesales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordline.onlinesales.enums.ClientType;
import jakarta.validation.constraints.NotNull;
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

        @NotNull
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

    }
}