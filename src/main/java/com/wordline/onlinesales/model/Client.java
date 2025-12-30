package com.wordline.onlinesales.model;
public abstract class Client {
    protected String clientId;

    public Client(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}