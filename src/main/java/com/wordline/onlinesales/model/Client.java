package com.wordline.onlinesales.model;

import com.wordline.onlinesales.enums.ClientType;
import lombok.Getter;

@Getter
public sealed abstract class Client  permits IndividualClient, ProfessionalClient  {

    protected final String clientId;

    protected Client(String clientId) {
        this.clientId = clientId;
    }

    public abstract String getDisplayName();
    public abstract ClientType getClientType();
}
