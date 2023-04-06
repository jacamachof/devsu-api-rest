package com.jacamachof.devsuapirest.mapper;

import com.jacamachof.devsuapirest.dto.AccountDto;
import com.jacamachof.devsuapirest.model.AccountEntity;
import com.jacamachof.devsuapirest.model.AccountType;
import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.StateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountMapper {

    private AccountMapper() {
        throw new AssertionError();
    }

    /**
     * Converts Account DTO object to Account entity object
     *
     * @param dto Account DTO object
     * @return entity Account entity
     */
    public static AccountEntity toEntity(AccountDto dto) {
        var clientEntity = new ClientEntity();
        var accountEntity = new AccountEntity();

        clientEntity.setIdentification(dto.getClientId());

        accountEntity.setClient(clientEntity);
        accountEntity.setAccountNumber(dto.getAccountNumber());
        accountEntity.setBalanceAvailable(dto.getBalanceAvailable());

        if (Objects.nonNull(dto.getId())) {
            accountEntity.setId(dto.getId());
        }

        if (Objects.nonNull(dto.getAccountType())) {
            accountEntity.setAccountType(AccountType.of(dto.getAccountType()).getValue());
        }

        if (Objects.nonNull(dto.getState())) {
            accountEntity.setState(StateEnum.of(dto.getState()).getValue());
        }

        return accountEntity;
    }

    /**
     * Converts Account entity object to Account DTO object
     *
     * @param entity Account entity object
     * @return dto Account dto object
     */
    public static AccountDto toDto(AccountEntity entity) {
        return AccountDto.builder()
                .id(entity.getId())
                .clientId(entity.getClient().getIdentification())
                .accountNumber(entity.getAccountNumber())
                .accountType(entity.getAccountType())
                .balanceAvailable(entity.getBalanceAvailable())
                .state(entity.getState()).build();
    }

    public static List<AccountDto> toDtoList(List<AccountEntity> entities) {
        var accounts = new ArrayList<AccountDto>();

        entities.forEach(entity -> accounts.add(toDto(entity)));

        return accounts;
    }
}
