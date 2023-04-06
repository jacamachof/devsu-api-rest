package com.jacamachof.devsuapirest.service;

import com.jacamachof.devsuapirest.config.AppConfig;
import com.jacamachof.devsuapirest.exception.ExceptionFactory;
import com.jacamachof.devsuapirest.model.AccountEntity;
import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    protected AppConfig appConfig;

    protected ExceptionFactory exceptionFactory;

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Autowired
    public void setExceptionFactory(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    protected void validateClientState(ClientEntity client) {
        if (StateEnum.INACTIVE.getValue().equals(client.getState())) {
            throw exceptionFactory.getClientInactiveException();
        }
    }

    protected void validateAccountState(AccountEntity account) {
        if (StateEnum.INACTIVE.getValue().equals(account.getState())) {
            throw exceptionFactory.getAccountInactiveException();
        }
    }
}
