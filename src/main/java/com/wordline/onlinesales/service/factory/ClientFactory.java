package com.wordline.onlinesales.service.factory;

import com.wordline.onlinesales.model.*;

import java.math.BigDecimal;

public class ClientFactory {

    public static Client create(CartRequest.ClientData data) {

        if (data == null || data.getType() == null) {
            throw new IllegalArgumentException("Client type is required");
        }

        return switch (data.getType()) {
            case INDIVIDUAL -> createIndividual(data);
            case PROFESSIONAL -> createProfessional(data);
        };
    }

    private static IndividualClient createIndividual(CartRequest.ClientData data) {
        if (data.getFirstName() == null || data.getLastName() == null) {
            throw new IllegalArgumentException(
                    "First and last name are required for individual clients");
        }
        return new IndividualClient(
                data.getClientId(),
                data.getFirstName(),
                data.getLastName()
        );
    }

    private static ProfessionalClient createProfessional(CartRequest.ClientData data) {
        if (data.getCompanyName() == null ||
                data.getAnnualRevenue() == null ||
                data.getBusinessRegistrationNumber() == null) {

            throw new IllegalArgumentException(
                    "Company name, revenue and registration number are required");
        }

        return new ProfessionalClient(
                data.getClientId(),
                data.getCompanyName(),
                BigDecimal.valueOf(data.getAnnualRevenue()),
                data.getBusinessRegistrationNumber(),
                data.getIntraCommunityVatNumber()
        );
    }
}
