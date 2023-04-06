package com.jacamachof.devsuapirest.service;

import com.jacamachof.devsuapirest.model.AccountEntity;
import com.jacamachof.devsuapirest.model.MovementEntity;
import com.jacamachof.devsuapirest.model.MovementType;
import com.jacamachof.devsuapirest.repository.AccountRepository;
import com.jacamachof.devsuapirest.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

interface Command {
    MovementEntity run(AccountEntity account, MovementEntity movement);
}

@Service
public class MovementService extends BaseService {

    private final Map<String, Command> commands =
            Map.of(MovementType.DEBIT.getValue(), this::withdrawMovement,
                    MovementType.CREDIT.getValue(), this::depositMovement);

    private AccountRepository accountRepository;

    private MovementRepository movementRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setMovementRepository(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    /**
     * Get movement by id
     *
     * @param id UUID
     * @return Movement entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when movement is not found
     */
    public MovementEntity getMovement(UUID id) {
        return movementRepository.findById(id).orElseThrow(() -> {
            throw exceptionFactory.getMovementNotFoundException();
        });
    }

    /**
     * Gets client's latest movements
     *
     * @param identification Client identification
     * @param page           Page
     * @param size           Size
     * @return Total count of movements and movements constrained by offset and limit
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when movements are not found
     */
    public Pair<Integer, List<MovementEntity>> getMovementsByClientIdentification(String identification, int page, int size) {
        var response = movementRepository.findByClient(identification, PageRequest.of(page, size));

        if (response.isEmpty()) {
            throw exceptionFactory.getMovementsNotFoundException();
        }

        return Pair.of(response.getTotalPages(), response.get().collect(Collectors.toList()));
    }

    /**
     * Gets client's bank statement
     *
     * @param identification Client identification
     * @param startDate      Start date
     * @param endDate        End date
     * @return Object containing the total debit, credit and movements per account
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the bank statement cannot be generated or
     *                                                                 the date range is greater than N days
     */
    public Pair<Pair<BigDecimal, BigDecimal>, Map<AccountEntity, List<MovementEntity>>> getBankStatementByClientAndRange(
            String identification, LocalDate startDate, LocalDate endDate) {

        var startDateTime = startDate.atTime(LocalTime.MIN);
        var endDateTime = endDate.atTime(LocalTime.MAX);

        if (Duration.between(startDateTime, endDateTime).toDays() > appConfig.getMaxDateRange()) {
            throw exceptionFactory.getStatementInvalidRangeException();
        }

        var databaseTuples = movementRepository.findByClientGroupByAccount(identification,
                startDateTime, endDateTime);

        if (databaseTuples.isEmpty()) {
            throw exceptionFactory.getStatementNotFoundException();
        }

        var groupedTuples =
                databaseTuples.stream().collect(Collectors.groupingBy(tuple -> tuple.get(0, AccountEntity.class)));

        var response = new HashMap<AccountEntity, List<MovementEntity>>();

        groupedTuples.forEach((key, value) -> response.put(key, value.stream().map(tuple ->
                tuple.get(1, MovementEntity.class)).collect(Collectors.toList())));

        return Pair.of(getTotal(response), response);
    }

    /**
     * Since the data to calculate the total amounts is already in memory, and run additional queries against the
     * database (even using multi-threading) may be slower due to network time (specially true when the database
     * and the service are not in the same network) and database processing, I decided to create a method to calculate
     * the total debit and credit using the database response
     */
    private Pair<BigDecimal, BigDecimal> getTotal(Map<AccountEntity, List<MovementEntity>> map) {
        var debit = BigDecimal.ZERO;
        var credit = BigDecimal.ZERO;

        for (var movements : map.values()) {
            for (var movement : movements) {
                if (MovementType.DEBIT.getValue().equals(movement.getMovementType())) {
                    debit = debit.add(movement.getMovementValue());
                }
                if (MovementType.CREDIT.getValue().equals(movement.getMovementType())) {
                    credit = credit.add(movement.getMovementValue());
                }
            }
        }

        return Pair.of(debit, credit);
    }

    /**
     * Performs a new movement using the given account number and movement details
     *
     * @param entity Movement entity
     * @return Movement results
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client or account is inactive, when the balance is
     *                                                                 insufficient,and when the daily withdrawal limit is reached
     */
    @Transactional
    public MovementEntity createMovement(MovementEntity entity) {
        var account = accountRepository.findByAccountNumber(
                entity.getAccount().getAccountNumber()).orElseThrow(() -> {
            throw exceptionFactory.getAccountNotFoundException();
        });

        validateClientState(account.getClient());
        validateAccountState(account);

        return commands.get(entity.getMovementType()).run(account, entity);
    }

    private MovementEntity withdrawMovement(AccountEntity account, MovementEntity movement) {
        if (account.getBalanceAvailable().compareTo(movement.getMovementValue()) < 0) {
            throw exceptionFactory.getNotBalanceException();
        }

        var amount = movementRepository.calculateTodayMovements(
                account.getClient().getId(), MovementType.DEBIT.getValue(),
                movement.getMovementDate().toLocalDate().atTime(LocalTime.MIN),
                movement.getMovementDate().toLocalDate().atTime(LocalTime.MAX));

        if (Objects.nonNull(amount) &&
                amount.add(movement.getMovementValue()).compareTo(appConfig.getDailyLimit()) > 0) {
            throw exceptionFactory.getDailyLimitException();
        }

        var before = account.getBalanceAvailable(); // BigDecimal is immutable
        var after = account.getBalanceAvailable().subtract(movement.getMovementValue());

        movement.setAccount(account);
        movement.setInitialBalance(before);
        movement.setBalanceAvailable(after);
        account.setBalanceAvailable(after);

        accountRepository.save(account);
        return movementRepository.save(movement);
    }

    private MovementEntity depositMovement(AccountEntity account, MovementEntity movement) {
        var before = account.getBalanceAvailable(); // BigDecimal is immutable
        var after = account.getBalanceAvailable().add(movement.getMovementValue());

        movement.setAccount(account);
        movement.setInitialBalance(before);
        movement.setBalanceAvailable(after);
        account.setBalanceAvailable(after);

        accountRepository.save(account);
        return movementRepository.save(movement);
    }
}
