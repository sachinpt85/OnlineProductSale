package com.wordline.onlinesales.model;

import com.wordline.onlinesales.enums.ClientType;

public final class IndividualClient extends Client {

    private final String firstName;
    private final String lastName;

    public IndividualClient(String clientId, String firstName, String lastName) {
        super(clientId);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    @Override
    public ClientType getClientType() {
        return ClientType.INDIVIDUAL;
    }
}
