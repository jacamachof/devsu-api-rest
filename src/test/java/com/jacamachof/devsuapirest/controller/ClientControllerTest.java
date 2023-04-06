package com.jacamachof.devsuapirest.controller;

import com.jacamachof.devsuapirest.exception.BusinessException;
import com.jacamachof.devsuapirest.exception.UnexpectedException;
import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.GenderEnum;
import com.jacamachof.devsuapirest.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private ClientController clientController;

    @Test
    void getClientTest() {
        when(clientService.getClient(any())).thenReturn(createRandomClient());

        var response = clientController.getClient(UUID.randomUUID().toString());

        assertNotNull(response);
    }

    @Test
    void getClient_NotNullTest() {
        assertThrows(ConstraintViolationException.class, () -> clientController.getClient(null));
    }

    @Test
    void getClient_BlankTest() {
        assertThrows(ConstraintViolationException.class, () -> clientController.getClient(""));
    }

    @Test
    void getClient_BusinessExceptionTest() {
        var identification = UUID.randomUUID().toString();

        when(clientService.getClient(any())).thenThrow(BusinessException.class);

        assertThrows(BusinessException.class, () -> clientController.getClient(identification));
    }

    @Test
    void getClient_UnexpectedException() {
        var identification = UUID.randomUUID().toString();

        when(clientService.getClient(any())).thenThrow(UnexpectedException.class);

        assertThrows(UnexpectedException.class, () -> clientController.getClient(identification));
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
