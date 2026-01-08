package com.wordline.onlinesales.service.pricing;

import com.wordline.onlinesales.enums.ProductType;
import com.wordline.onlinesales.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    public BigDecimal getPrice(Client client, ProductType product) {

        if (client instanceof IndividualClient) {
            return switch (product) {
                case HIGH_END_PHONE -> BigDecimal.valueOf(1500);
                case MID_RANGE_PHONE -> BigDecimal.valueOf(800);
                case LAPTOP -> BigDecimal.valueOf(1200);
            };
        }

        if (client instanceof ProfessionalClient professional) {
            boolean highRevenue = professional.isHighRevenueClient();

            return switch (product) {
                case HIGH_END_PHONE -> highRevenue
                        ? BigDecimal.valueOf(1000)
                        : BigDecimal.valueOf(1150);
                case MID_RANGE_PHONE -> highRevenue
                        ? BigDecimal.valueOf(550)
                        : BigDecimal.valueOf(600);
                case LAPTOP -> highRevenue
                        ? BigDecimal.valueOf(900)
                        : BigDecimal.valueOf(1000);
            };
        }

        throw new IllegalArgumentException("Unknown client type");
    }
}
