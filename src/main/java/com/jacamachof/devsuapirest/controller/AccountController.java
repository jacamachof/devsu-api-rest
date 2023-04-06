package com.jacamachof.devsuapirest.controller;

import com.jacamachof.devsuapirest.dto.AccountDto;
import com.jacamachof.devsuapirest.exception.BusinessException;
import com.jacamachof.devsuapirest.exception.ExceptionFactory;
import com.jacamachof.devsuapirest.mapper.AccountMapper;
import com.jacamachof.devsuapirest.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/cuentas")
public class AccountController {

    private AccountService accountService;

    private ExceptionFactory exceptionFactory;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setExceptionFactory(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    @GetMapping("/obtener")
    public AccountDto getAccount(@RequestParam @NotBlank String accountNumber) {
        AccountDto response;

        log.info("Getting account {} information", accountNumber);

        try {
            response = AccountMapper.toDto(accountService.getAccount(accountNumber));
        } catch (BusinessException e) {
            log.warn("Account {} not found. {}", accountNumber, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to get account {} with message {}",
                    e.getClass().getSimpleName(), accountNumber, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Account {} information found {}", accountNumber, response);

        return response;
    }

    @GetMapping("/obtener/cliente")
    public List<AccountDto> getAccountsByClientId(@RequestParam @NotBlank String id) {
        List<AccountDto> response;

        log.info("Getting accounts by client identification {}", id);

        try {
            response = AccountMapper.toDtoList(accountService.getAccountsByClientIdentification(id));
        } catch (BusinessException e) {
            log.warn("Accounts of client {} not found. {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to get account of client {} with message {}",
                    e.getClass().getSimpleName(), id, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Accounts of client {} found {}", id, response.size());

        return response;
    }

    @PostMapping("/crear")
    public void createAccount(@Valid @RequestBody AccountDto dto) {
        log.info("Creating account {} with the following information {}", dto.getAccountNumber(), dto);

        try {
            accountService.createAccount(AccountMapper.toEntity(dto));
        } catch (BusinessException e) {
            log.warn("Account {} not created. {}", dto.getAccountNumber(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to create account {} with message {}",
                    e.getClass().getSimpleName(), dto.getAccountNumber(), e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Account {} created with the following information {}", dto.getAccountNumber(), dto);
    }

    @PutMapping("/actualizar")
    public void updateAccount(@Valid @RequestBody AccountDto dto) {
        log.info("Updating account {} with the following information {}", dto.getAccountNumber(), dto);

        try {
            accountService.updateAccount(AccountMapper.toEntity(dto));
        } catch (BusinessException e) {
            log.warn("Could not update account {}. {}", dto.getAccountNumber(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to update account {} with message {}",
                    e.getClass().getSimpleName(), dto.getAccountNumber(), e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Account {} updated with the following information {}", dto.getAccountNumber(), dto);
    }

    @DeleteMapping("/eliminar")
    public void deleteAccount(@RequestParam @NotBlank String accountNumber) {
        log.info("Deleting account {}", accountNumber);

        try {
            accountService.deleteAccount(accountNumber);
        } catch (BusinessException e) {
            log.warn("Could not delete account {}. {}", accountNumber, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to delete account {} with message {}",
                    e.getClass().getSimpleName(), accountNumber, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Account {} deleted", accountNumber);
    }
}
