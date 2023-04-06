package com.jacamachof.devsuapirest.service;

import com.jacamachof.devsuapirest.model.AccountEntity;
import com.jacamachof.devsuapirest.repository.AccountRepository;
import com.jacamachof.devsuapirest.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountService extends BaseService {

    private ClientRepository clientRepository;

    private AccountRepository accountRepository;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Gets account's information by the account number
     *
     * @param accountNumber Accout number
     * @return Account entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the account is not found
     */
    public AccountEntity getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> {
            throw exceptionFactory.getAccountNotFoundException();
        });
    }

    /**
     * Gets accounts of the given client identification
     *
     * @param identification Client identification
     * @return List of accounts
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when accounts are not found
     */
    public List<AccountEntity> getAccountsByClientIdentification(String identification) {
        var accounts = accountRepository.findByClientIdentification(identification);

        if (accounts.isEmpty()) {
            throw exceptionFactory.getAccountsNotFoundException();
        }

        return accounts;
    }

    /**
     * Creates a new account for the given client.
     *
     * @param accountEntity Account entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client was not found, the client is
     *                                                                 inactive or the account number already exists
     */
    @Transactional
    public void createAccount(AccountEntity accountEntity) {
        var clientEntity = clientRepository.findByIdentification(
                accountEntity.getClient().getIdentification()).orElseThrow(() -> {
            throw exceptionFactory.getClientNotFoundException();
        });

        validateClientState(clientEntity);

        if (accountRepository.existsByAccountNumber(accountEntity.getAccountNumber())) {
            throw exceptionFactory.getAccountExistsException();
        }

        accountEntity.setClient(clientEntity);
        accountRepository.save(accountEntity);
    }

    /**
     * Delete account
     *
     * @param accountNumber Account number
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the account does not exist
     */
    @Transactional
    public void deleteAccount(String accountNumber) {
        var entity = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> {
            throw exceptionFactory.getAccountNotFoundException();
        });

        validateClientState(entity.getClient());
        accountRepository.delete(entity);
    }

    /**
     * Updates account information and balance excepting ownership and account number
     *
     * @param entity Account entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the account is not found
     */
    @Transactional
    public void updateAccount(AccountEntity entity) {
        var db = accountRepository.findByAccountNumber(entity.getAccountNumber()).orElseThrow(() -> {
            throw exceptionFactory.getAccountNotFoundException();
        });

        validateClientState(db.getClient());
        db.setAccountType(entity.getAccountType());
        db.setBalanceAvailable(entity.getBalanceAvailable());
        db.setState(entity.getState());

        accountRepository.save(db);
    }
}
