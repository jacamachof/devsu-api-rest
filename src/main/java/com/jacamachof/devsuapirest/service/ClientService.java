package com.jacamachof.devsuapirest.service;

import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.StateEnum;
import com.jacamachof.devsuapirest.repository.ClientRepository;
import com.jacamachof.devsuapirest.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ClientService extends BaseService {

    private ClientRepository clientRepository;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Creates a new client in the database. Encodes sensible information prior to persist
     *
     * @param entity Client entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client already exists
     */
    @Transactional
    public void createClient(ClientEntity entity) {
        if (clientRepository.existsByIdentification(entity.getIdentification())) {
            throw exceptionFactory.getClientExistsException();
        }

        entity.setPassword(SecurityUtils.encode(entity.getPassword()));
        clientRepository.save(entity);
    }

    /**
     * Change client's state from ACTIVE to INACTIVE representing a temporary suspension of the client
     * and his accounts to perform operations. To reactivate it, update the client information
     *
     * @param identification Client identification
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client is not found
     */
    @Transactional
    public void deactivateClient(String identification) {
        var entity = clientRepository.findByIdentification(identification).orElseThrow(() -> {
            throw exceptionFactory.getClientNotFoundException();
        });

        entity.setState(StateEnum.INACTIVE.getValue());
        clientRepository.save(entity);
    }

    /**
     * Gets client's information by his identification
     *
     * @param identification Client identification
     * @return Client entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client is not found
     */
    public ClientEntity getClient(String identification) {
        return clientRepository.findByIdentification(identification).orElseThrow(() -> {
            throw exceptionFactory.getClientNotFoundException();
        });
    }

    /**
     * Validates that the given password matches the existing password in the database, then
     * the method changes client's password in the database using the new given password
     *
     * @param entity Client entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client is not found or the password
     *                                                                 does not match
     */
    @Transactional
    public void changePassword(ClientEntity entity) {
        var db = clientRepository.findByIdentification(entity.getIdentification()).orElseThrow(() -> {
            throw exceptionFactory.getClientNotFoundException();
        });

        if (SecurityUtils.matches(entity.getPassword(), db.getPassword())) {
            db.setPassword(SecurityUtils.encode(entity.getNewPassword()));
        } else {
            throw exceptionFactory.getPasswordInvalidException();
        }

        clientRepository.save(db);
    }

    /**
     * Hard-delete the client in the database. The hard-deletion includes the hard-deletion of his accounts and
     * movements. It is recommended to back up the information previously in a data warehouse or any other mechanism
     *
     * @param identification Client identification
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client is not found
     */
    @Transactional
    public void deleteClient(String identification) {
        var entity = clientRepository.findByIdentification(identification).orElseThrow(() -> {
            throw exceptionFactory.getClientNotFoundException();
        });

        clientRepository.delete(entity);
    }

    /**
     * Updates client information excepting password and client's identification
     *
     * @param entity Client entity
     * @throws com.jacamachof.devsuapirest.exception.BusinessException when the client is not found
     */
    @Transactional
    public void updateClient(ClientEntity entity) {
        var db = clientRepository.findByIdentification(entity.getIdentification()).orElseThrow(() -> {
            throw exceptionFactory.getClientNotFoundException();
        });

        db.setState(entity.getState());
        db.setName(entity.getName());
        db.setAddress(entity.getAddress());
        db.setPhoneNumber(entity.getPhoneNumber());
        db.setGender(entity.getGender());
        db.setBirthdate(entity.getBirthdate());

        clientRepository.save(db);
    }
}
