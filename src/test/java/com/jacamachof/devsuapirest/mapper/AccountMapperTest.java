package com.jacamachof.devsuapirest.mapper;

import com.jacamachof.devsuapirest.dto.AccountDto;
import com.jacamachof.devsuapirest.model.AccountEntity;
import com.jacamachof.devsuapirest.model.AccountType;
import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.StateEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    @Test
    void toEntityTest() {
        var dto = AccountDto.builder()
                .id(UUID.randomUUID())
                .clientId(UUID.randomUUID().toString())
                .accountNumber(UUID.randomUUID().toString())
                .accountType(AccountType.CHECKING.getValue())
                .balanceAvailable(BigDecimal.ZERO)
                .state(StateEnum.ACTIVE.getValue()).build();

        var entity = AccountMapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getClientId(), entity.getClient().getIdentification());
        assertEquals(dto.getAccountNumber(), entity.getAccountNumber());
        assertEquals(dto.getAccountType(), entity.getAccountType());
        assertEquals(dto.getBalanceAvailable(), entity.getBalanceAvailable());
        assertEquals(dto.getState(), entity.getState());
    }

    @Test
    void toDtoTest() {
        var entity = new AccountEntity();
        var client = new ClientEntity();
        client.setIdentification(UUID.randomUUID().toString());
        entity.setClient(client);
        entity.setAccountNumber(UUID.randomUUID().toString());
        entity.setAccountType(AccountType.CHECKING.getValue());
        entity.setBalanceAvailable(BigDecimal.ZERO);
        entity.setState(StateEnum.ACTIVE.getValue());

        var dto = AccountMapper.toDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getClient().getIdentification(), dto.getClientId());
        assertEquals(entity.getAccountNumber(), dto.getAccountNumber());
        assertEquals(entity.getAccountType(), dto.getAccountType());
        assertEquals(entity.getBalanceAvailable(), dto.getBalanceAvailable());
        assertEquals(entity.getState(), dto.getState());
    }
}
