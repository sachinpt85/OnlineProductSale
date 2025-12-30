package com.wordline.onlinesales.model;

import lombok.Getter;

@Getter
public class ProfessionalClient extends Client {
    private String companyName;
    private double annualRevenue;
    private String businessRegistrationNumber;
    private String intraCommunityVatNumber;

    public ProfessionalClient(String clientId, String companyName,
                              double annualRevenue, String businessRegistrationNumber) {
        this(clientId, companyName, annualRevenue, businessRegistrationNumber, null);
    }

    public ProfessionalClient(String clientId, String companyName,
                              double annualRevenue, String businessRegistrationNumber,
                              String intraCommunityVatNumber) {
        super(clientId);
        this.companyName = companyName;
        this.annualRevenue = annualRevenue;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.intraCommunityVatNumber = intraCommunityVatNumber;
    }

    public boolean isHighRevenueClient() {
        return annualRevenue > 10_000_000;
    }
}