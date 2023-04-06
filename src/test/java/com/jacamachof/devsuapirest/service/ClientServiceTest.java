package com.jacamachof.devsuapirest.service;

import com.jacamachof.devsuapirest.exception.BadRequestException;
import com.jacamachof.devsuapirest.exception.NotFoundException;
import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.GenderEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    void createClientTest() {
        assertDoesNotThrow(() -> clientService.createClient(createRandomClient()));
    }

    @Test
    void createExistingClientTest() {
        var client = createRandomClient();

        clientService.createClient(client);

        assertThrows(BadRequestException.class, () -> clientService.createClient(client));
    }

    @Test
    void getClientTest() {
        var client = createRandomClient();
        client.setNewPassword(client.getPassword());

        clientService.createClient(client);

        var response = clientService.getClient(client.getIdentification());

        assertEquals(client.getId(), response.getId());
        assertEquals(client.getName(), response.getName());
        assertEquals(client.getAddress(), response.getAddress());
        assertNotEquals(client.getNewPassword(), response.getPassword());
    }

    @Test
    void getClient_NotFoundTest() {
        var client = createRandomClient();
        var identification = client.getIdentification();

        assertThrows(NotFoundException.class, () -> clientService.getClient(identification));
    }

    @Test
    void updateClientTest() {
        var client = createRandomClient();
        clientService.createClient(client);

        var updated = createRandomClient();
        updated.setId(client.getId());
        updated.setIdentification(client.getIdentification());
        clientService.updateClient(updated);

        var response = clientService.getClient(client.getIdentification());
        assertNotEquals(client.getName(), response.getName());
        assertNotEquals(client.getAddress(), response.getAddress());
        assertNotEquals(client.getPhoneNumber(), response.getPhoneNumber());
    }

    @Test
    void updateClient_NotFoundTest() {
        var client = createRandomClient();

        assertThrows(NotFoundException.class, () -> clientService.updateClient(client));
    }

    @Test
    void deleteClientTest() {
        var client = createRandomClient();
        var identification = client.getIdentification();

        clientService.createClient(client);
        clientService.deleteClient(identification);

        assertThrows(NotFoundException.class, () -> clientService.getClient(identification));
    }

    @Test
    void deleteClient_NotFoundTest() {
        var client = createRandomClient();
        var identification = client.getIdentification();

        assertThrows(NotFoundException.class, () -> clientService.deleteClient(identification));
    }

    private ClientEntity createRandomClient() {
        var client = new ClientEntity();
        client.setIdentification(UUID.randomUUID().toString());
        client.setName(UUID.randomUUID().toString());
        client.setAddress(UUID.randomUUID().toString());
        client.setPhoneNumber(UUID.randomUUID().toString());
        client.setGender(GenderEnum.MALE.getValue());
        client.setBirthdate(LocalDate.now().minusYears(20));
        client.setPassword(UUID.randomUUID().toString());

        return client;
    }
}
