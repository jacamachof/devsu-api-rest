package com.jacamachof.devsuapirest.mapper;

import com.jacamachof.devsuapirest.dto.*;
import com.jacamachof.devsuapirest.model.AccountEntity;
import com.jacamachof.devsuapirest.model.MovementEntity;
import com.jacamachof.devsuapirest.model.MovementType;
import com.jacamachof.devsuapirest.model.StateEnum;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MovementMapper {

    private MovementMapper() {
        throw new AssertionError();
    }

    /**
     * Converts Movement DTO object to Movement entity object
     *
     * @param dto Movement DTO object
     * @return entity Movement entity
     */
    public static MovementEntity toEntity(MovementDto dto) {
        var accountEntity = new AccountEntity();
        var movementEntity = new MovementEntity();

        accountEntity.setAccountNumber(dto.getAccountId());

        movementEntity.setAccount(accountEntity);
        movementEntity.setMovementValue(dto.getMovementValue());

        if (Objects.nonNull(dto.getId())) {
            movementEntity.setId(dto.getId());
        }

        if (Objects.nonNull(dto.getMovementType())) {
            movementEntity.setMovementType(MovementType.of(dto.getMovementType()).getValue());
        }

        if (Objects.nonNull(dto.getState())) {
            movementEntity.setState(StateEnum.of(dto.getState()).getValue());
        }

        return movementEntity;
    }

    /**
     * Converts Movement entity object to Movement DTO object
     *
     * @param entity Movement entity object
     * @return dto Movement dto object
     */
    public static MovementDto toDto(MovementEntity entity) {
        return MovementDto.builder()
                .id(entity.getId())
                .accountId(entity.getAccount().getAccountNumber())
                .movementDate(entity.getMovementDate())
                .movementType(entity.getMovementType())
                .movementValue(entity.getMovementValue())
                .initialBalance(entity.getInitialBalance())
                .balanceAvailable(entity.getBalanceAvailable())
                .state(entity.getState()).build();
    }

    public static List<MovementDto> toDtoList(List<MovementEntity> entities) {
        var accounts = new ArrayList<MovementDto>();

        entities.forEach(entity -> accounts.add(toDto(entity)));

        return accounts;
    }

    public static ClientMovementsDto toClientMovementDto(Pair<Integer, List<MovementEntity>> tuple) {
        var list = new ArrayList<MovementDto>();

        tuple.getSecond().forEach(entity -> list.add(toDto(entity)));

        return ClientMovementsDto.builder()
                .pages(tuple.getFirst())
                .movements(list).build();
    }

    public static AccountStatementDto toAccountStatementDto(AccountEntity account, List<MovementEntity> movements) {

        return AccountStatementDto.builder()
                .id(account.getId())
                .clientId(account.getClient().getIdentification())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balanceAvailable(account.getBalanceAvailable())
                .state(account.getState())
                .movements(toDtoList(movements)).build();
    }

    public static List<AccountStatementDto> toAccountStatementDtoList(Map<AccountEntity, List<MovementEntity>> map) {
        var list = new ArrayList<AccountStatementDto>();

        map.forEach((key, value) -> list.add(toAccountStatementDto(key, value)));

        return list;
    }

    public static BankStatementDto toBankStatementDto(
            Pair<Pair<BigDecimal, BigDecimal>, Map<AccountEntity, List<MovementEntity>>> bankStatement) {

        return BankStatementDto.builder()
                .totalDebit(bankStatement.getFirst().getFirst())
                .totalCredit(bankStatement.getFirst().getSecond())
                .accounts(toAccountStatementDtoList(bankStatement.getSecond())).build();
    }
}
