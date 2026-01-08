package com.wordline.onlinesales.model;

import com.wordline.onlinesales.enums.ClientType;

import java.math.BigDecimal;

public class ProfessionalClient extends Client {

    private static final BigDecimal HIGH_REVENUE_THRESHOLD =
            BigDecimal.valueOf(10_000_000);

    private final String companyName;
    private final BigDecimal annualRevenue;
    private final String businessRegistrationNumber;
    private final String intraCommunityVatNumber;

    public ProfessionalClient(
            String clientId,
            String companyName,
            BigDecimal annualRevenue,
            String businessRegistrationNumber,
            String intraCommunityVatNumber) {

        super(clientId);
        this.companyName = companyName;
        this.annualRevenue = annualRevenue;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.intraCommunityVatNumber = intraCommunityVatNumber;
    }

    public boolean isHighRevenueClient() {
        return annualRevenue != null
                && annualRevenue.compareTo(HIGH_REVENUE_THRESHOLD) > 0;
    }

    @Override
    public String getDisplayName() {
        return companyName;
    }

    @Override
    public ClientType getClientType() {
        return ClientType.PROFESSIONAL;
    }
}
