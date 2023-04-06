package com.jacamachof.devsuapirest.repository;

import com.jacamachof.devsuapirest.model.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, UUID> {

    Optional<ClientEntity> findByIdentification(String identification);

    boolean existsByIdentification(String identification);

    void deleteByIdentification(String identification);
}
