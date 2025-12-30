package com.wordline.onlinesales.component;

import com.wordline.onlinesales.enums.ClientType;
import com.wordline.onlinesales.model.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CartResponseBuilder {

    public CartResponse build(Client client, BigDecimal totalAmount) {
        String clientName = getClientName(client);
        String clientType = getClientType(client).name();
        BigDecimal formattedTotal = formatAmount(totalAmount);

        return new CartResponse(
                client.getClientId(),
                clientName,
                clientType,
                formattedTotal,
                "EUR"
        );
    }

    private String getClientName(Client client) {
        if (client instanceof IndividualClient individual) {
            return individual.getFullName();
        } else if (client instanceof ProfessionalClient professional) {
            return professional.getCompanyName();
        }
        return "Unknown";
    }

    private ClientType getClientType(Client client) {
        if (client instanceof IndividualClient) {
            return ClientType.INDIVIDUAL;
        } else if (client instanceof ProfessionalClient) {
            return ClientType.PROFESSIONAL;
        }
        throw new IllegalArgumentException("Unknown client type");
    }

    private BigDecimal formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}