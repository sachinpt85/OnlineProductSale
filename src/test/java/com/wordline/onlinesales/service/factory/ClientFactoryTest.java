package com.wordline.onlinesales.service.factory;

import com.wordline.onlinesales.enums.ClientType;
import com.wordline.onlinesales.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientFactoryTest {

    @Test
    void shouldCreateIndividualClient() {
        CartRequest.ClientData data = new CartRequest.ClientData();
        data.setType(ClientType.INDIVIDUAL);
        data.setClientId("IND001");
        data.setFirstName("John");
        data.setLastName("Doe");

        Client client = ClientFactory.create(data);

        assertNotNull(client);
        assertInstanceOf(IndividualClient.class, client);
        assertEquals("IND001", client.getClientId());
        assertEquals("John Doe", client.getDisplayName());
        assertEquals(ClientType.INDIVIDUAL, client.getClientType());
    }

    @Test
    void shouldCreateProfessionalClient_HighRevenue() {
        CartRequest.ClientData data = new CartRequest.ClientData();
        data.setType(ClientType.PROFESSIONAL);
        data.setClientId("PROF001");
        data.setCompanyName("TechCorp");
        data.setAnnualRevenue(15_000_000.0);
        data.setBusinessRegistrationNumber("BRN001");

        Client client = ClientFactory.create(data);

        assertNotNull(client);
        assertInstanceOf(ProfessionalClient.class, client);
        assertEquals("PROF001", client.getClientId());
        assertEquals("TechCorp", client.getDisplayName());
        assertEquals(ClientType.PROFESSIONAL, client.getClientType());

        ProfessionalClient professional = (ProfessionalClient) client;
        assertTrue(professional.isHighRevenueClient());
    }

    @Test
    void shouldCreateProfessionalClient_LowRevenue() {
        CartRequest.ClientData data = new CartRequest.ClientData();
        data.setType(ClientType.PROFESSIONAL);
        data.setClientId("PROF002");
        data.setCompanyName("SmallBiz");
        data.setAnnualRevenue(5_000_000.0);
        data.setBusinessRegistrationNumber("BRN002");

        ProfessionalClient client =
                (ProfessionalClient) ClientFactory.create(data);

        assertFalse(client.isHighRevenueClient());
    }

    @Test
    void shouldThrowException_WhenClientDataIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ClientFactory.create(null)
        );

        assertEquals("Client type is required", ex.getMessage());
    }

    @Test
    void shouldThrowException_WhenClientTypeIsNull() {
        CartRequest.ClientData data = new CartRequest.ClientData();
        data.setClientId("TEST001");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ClientFactory.create(data)
        );

        assertEquals("Client type is required", ex.getMessage());
    }

    @Test
    void shouldThrowException_WhenIndividualFieldsMissing() {
        CartRequest.ClientData data = new CartRequest.ClientData();
        data.setType(ClientType.INDIVIDUAL);
        data.setClientId("IND002");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ClientFactory.create(data)
        );

        assertTrue(ex.getMessage().contains("First and last name"));
    }

    @Test
    void shouldThrowException_WhenProfessionalFieldsMissing() {
        CartRequest.ClientData data = new CartRequest.ClientData();
        data.setType(ClientType.PROFESSIONAL);
        data.setClientId("PROF003");
        data.setCompanyName("Corp");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ClientFactory.create(data)
        );

        assertTrue(ex.getMessage().contains("Company name, revenue"));
    }
}
