package com.wordline.onlinesales.model;

import com.wordline.onlinesales.enums.ClientType;
import lombok.Getter;

@Getter
public abstract class Client {

    protected final String clientId;

    protected Client(String clientId) {
        this.clientId = clientId;
    }

    public abstract String getDisplayName();
    public abstract ClientType getClientType();
}
