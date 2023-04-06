package com.jacamachof.devsuapirest.controller;

import com.jacamachof.devsuapirest.dto.ChangePasswordDto;
import com.jacamachof.devsuapirest.dto.ClientDto;
import com.jacamachof.devsuapirest.exception.BusinessException;
import com.jacamachof.devsuapirest.exception.ExceptionFactory;
import com.jacamachof.devsuapirest.mapper.ClientMapper;
import com.jacamachof.devsuapirest.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("/clientes")
public class ClientController {

    private ClientService clientService;

    private ExceptionFactory exceptionFactory;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setExceptionFactory(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    @GetMapping("/obtener")
    public ClientDto getClient(@RequestParam @NotBlank String id) {
        ClientDto response;

        log.info("Getting client {} information", id);

        try {
            response = ClientMapper.toDto(clientService.getClient(id));
        } catch (BusinessException e) {
            log.warn("Client {} not found. {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to get client {} with message {}",
                    e.getClass().getSimpleName(), id, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Client {} information found {}", id, response);

        return response;
    }

    @PostMapping("/crear")
    public void createClient(@Valid @RequestBody ClientDto dto) {
        log.info("Creating client {} with the following information {}", dto.getIdentification(), dto);

        try {
            clientService.createClient(ClientMapper.toEntity(dto));
        } catch (BusinessException e) {
            log.warn("Client {} not created. {}", dto.getIdentification(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to create client {} with message {}",
                    e.getClass().getSimpleName(), dto.getIdentification(), e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Client {} created with the following information {}", dto.getIdentification(), dto);
    }

    @PatchMapping("/desactivar")
    public void deactivateClient(@RequestParam @NotBlank String id) {
        log.info("Deactivating client {}", id);

        try {
            clientService.deactivateClient(id);
        } catch (BusinessException e) {
            log.warn("Client {} not deactivated. {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to deactivate client {} with message {}",
                    e.getClass().getSimpleName(), id, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Client {} deactivated", id);
    }

    @PatchMapping("/credenciales")
    public void changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        log.info("Changing password of the client {}", dto.getIdentification());

        try {
            clientService.changePassword(ClientMapper.toChangePassword(dto));
        } catch (BusinessException e) {
            log.warn("Could not change {} client's password. {}", dto.getIdentification(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to change {} client's password with message {}",
                    e.getClass().getSimpleName(), dto.getIdentification(), e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Password of the client {} changed", dto.getIdentification());
    }

    @PutMapping("/actualizar")
    public void updateClient(@Valid @RequestBody ClientDto dto) {
        log.info("Updating client {} with the following information {}", dto.getIdentification(), dto);

        try {
            clientService.updateClient(ClientMapper.toEntity(dto));
        } catch (BusinessException e) {
            log.warn("Could not update client {}. {}", dto.getIdentification(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to update client {} with message {}",
                    e.getClass().getSimpleName(), dto.getIdentification(), e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Client {} updated with the following information {}", dto.getIdentification(), dto);
    }

    @DeleteMapping("/eliminar")
    public void deleteClient(@RequestParam @NotBlank String id) {
        log.info("Deleting client {}", id);

        try {
            clientService.deleteClient(id);
        } catch (BusinessException e) {
            log.warn("Could not delete client {}. {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to delete client {} with message {}",
                    e.getClass().getSimpleName(), id, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Client {} deleted", id);
    }

    //TODO: Unit Tests
    //TODO: Integration Tests (Postman)
}
