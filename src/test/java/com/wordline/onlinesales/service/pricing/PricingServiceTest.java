package com.wordline.onlinesales.service.pricing;

import com.wordline.onlinesales.enums.ProductType;
import com.wordline.onlinesales.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService(); // ✅ pure unit test
    }


    @Test
    void individualClient_highEndPhonePrice() {
        Client client = createIndividualClient();

        BigDecimal price =
                pricingService.getPrice(client, ProductType.HIGH_END_PHONE);

        assertEquals(new BigDecimal("1500"), price);
    }

    @Test
    void individualClient_midRangePhonePrice() {
        Client client = createIndividualClient();

        BigDecimal price =
                pricingService.getPrice(client, ProductType.MID_RANGE_PHONE);

        assertEquals(new BigDecimal("800"), price);
    }

    @Test
    void individualClient_laptopPrice() {
        Client client = createIndividualClient();

        BigDecimal price =
                pricingService.getPrice(client, ProductType.LAPTOP);

        assertEquals(new BigDecimal("1200"), price);
    }

    // ---------- PROFESSIONAL HIGH REVENUE ----------

    @Test
    void professionalHighRevenue_highEndPhonePrice() {
        Client client = createProfessionalClient(15_000_000);

        BigDecimal price =
                pricingService.getPrice(client, ProductType.HIGH_END_PHONE);

        assertEquals(new BigDecimal("1000"), price);
    }

    @Test
    void professionalHighRevenue_midRangePhonePrice() {
        Client client = createProfessionalClient(15_000_000);

        BigDecimal price =
                pricingService.getPrice(client, ProductType.MID_RANGE_PHONE);

        assertEquals(new BigDecimal("550"), price);
    }

    @Test
    void professionalHighRevenue_laptopPrice() {
        Client client = createProfessionalClient(15_000_000);

        BigDecimal price =
                pricingService.getPrice(client, ProductType.LAPTOP);

        assertEquals(new BigDecimal("900"), price);
    }

    // ---------- PROFESSIONAL LOW REVENUE ----------

    @Test
    void professionalLowRevenue_highEndPhonePrice() {
        Client client = createProfessionalClient(5_000_000);

        BigDecimal price =
                pricingService.getPrice(client, ProductType.HIGH_END_PHONE);

        assertEquals(new BigDecimal("1150"), price);
    }

    @Test
    void professionalLowRevenue_midRangePhonePrice() {
        Client client = createProfessionalClient(5_000_000);

        BigDecimal price =
                pricingService.getPrice(client, ProductType.MID_RANGE_PHONE);

        assertEquals(new BigDecimal("600"), price);
    }

    @Test
    void professionalLowRevenue_laptopPrice() {
        Client client = createProfessionalClient(5_000_000);

        BigDecimal price =
                pricingService.getPrice(client, ProductType.LAPTOP);

        assertEquals(new BigDecimal("1000"), price);
    }


    // ---------- HELPERS ----------

    private Client createIndividualClient() {
        return new IndividualClient(
                "IND001",
                "John",
                "Doe"
        );
    }

    private Client createProfessionalClient(double revenue) {
        return new ProfessionalClient(
                "PROF001",
                "TechCorp",
                BigDecimal.valueOf(revenue),
                "BRN001",
                null
        );
    }
}
