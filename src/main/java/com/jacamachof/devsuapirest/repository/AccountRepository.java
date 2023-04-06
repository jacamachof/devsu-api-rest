package com.jacamachof.devsuapirest.repository;

import com.jacamachof.devsuapirest.model.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    List<AccountEntity> findByClientIdentification(String identification);

    boolean existsByAccountNumber(String accountNumber);

    void deleteByAccountNumber(String accountNumber);
}
