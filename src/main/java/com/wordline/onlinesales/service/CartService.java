package com.wordline.onlinesales.service;

import com.wordline.onlinesales.enums.ClientType;
import com.wordline.onlinesales.enums.ProductType;
import com.wordline.onlinesales.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CartService {

    // Convert request to domain Client
    public Client createClientFromRequest(CartRequest.ClientData clientData) {
        ClientType type = clientData.getType(); // Now it's ClientType, not String
        String clientId = clientData.getClientId();

        if (type == null) {
            throw new IllegalArgumentException("Client type is required");
        }

        // No need for try-catch anymore, Jackson already validated it
        return switch (type) {
            case INDIVIDUAL -> createIndividualClient(clientId, clientData);
            case PROFESSIONAL -> createProfessionalClient(clientId, clientData);
        };
    }

    private IndividualClient createIndividualClient(String clientId, CartRequest.ClientData clientData) {
        if (clientData.getFirstName() == null || clientData.getLastName() == null) {
            throw new IllegalArgumentException("First name and last name are required for individual clients");
        }
        return new IndividualClient(
                clientId,
                clientData.getFirstName(),
                clientData.getLastName()
        );
    }

    private ProfessionalClient createProfessionalClient(String clientId, CartRequest.ClientData clientData) {
        if (clientData.getCompanyName() == null ||
                clientData.getAnnualRevenue() == null ||
                clientData.getBusinessRegistrationNumber() == null) {
            throw new IllegalArgumentException("Company name, annual revenue, and business registration number are required for professional clients");
        }
        return new ProfessionalClient(
                clientId,
                clientData.getCompanyName(),
                clientData.getAnnualRevenue(),
                clientData.getBusinessRegistrationNumber(),
                clientData.getIntraCommunityVatNumber() // can be null
        );
    }

    // Get price based on client and product
    private BigDecimal getPrice(Client client, String productTypeStr) {
        if (productTypeStr == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }

        try {
            ProductType productType = ProductType.valueOf(productTypeStr.toUpperCase());

            if (client instanceof IndividualClient) {
                return switch (productType) {
                    case HIGH_END_PHONE -> BigDecimal.valueOf(1500.00);
                    case MID_RANGE_PHONE -> BigDecimal.valueOf(800.00);
                    case LAPTOP -> BigDecimal.valueOf(1200.00);
                };
            }

            if (client instanceof ProfessionalClient professional) {
                boolean highRevenue = professional.isHighRevenueClient();

                return switch (productType) {
                    case HIGH_END_PHONE -> highRevenue
                            ? BigDecimal.valueOf(1000.00)
                            : BigDecimal.valueOf(1150.00);
                    case MID_RANGE_PHONE -> highRevenue
                            ? BigDecimal.valueOf(550.00)
                            : BigDecimal.valueOf(600.00);
                    case LAPTOP -> highRevenue
                            ? BigDecimal.valueOf(900.00)
                            : BigDecimal.valueOf(1000.00);
                };
            }

            throw new IllegalArgumentException("Unknown client type");

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product type: " + productTypeStr +
                    ". Valid values: HIGH_END_PHONE, MID_RANGE_PHONE, LAPTOP");
        }
    }

    // Calculate total for cart
    public CartResponse calculateTotal(CartRequest request) {
        if (request == null || request.getClient() == null) {
            throw new IllegalArgumentException("Request and client data are required");
        }

        var client = createClientFromRequest(request.getClient());

        BigDecimal total = BigDecimal.ZERO;
        Map<String, Integer> items = request.getItems();

        if (items != null) {
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                String productType = entry.getKey();
                Integer quantity = entry.getValue();

                if (quantity != null && quantity > 0) {
                    BigDecimal price = getPrice(client, productType);
                    BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));
                    total = total.add(itemTotal);
                }
            }
        }

        total = total.setScale(2, RoundingMode.HALF_UP);

        String clientName;
        ClientType clientType;

        if (client instanceof IndividualClient individual) {
            clientName = individual.getFullName();
            clientType = ClientType.INDIVIDUAL;
        } else if (client instanceof ProfessionalClient professional) {
            clientName = professional.getCompanyName();
            clientType = ClientType.PROFESSIONAL;
        } else {
            clientName = "Unknown";
            clientType = null;
        }

        return new CartResponse(
                client.getClientId(),
                clientName,
                clientType.name(),
                total,
                "EUR"
        );
    }
}