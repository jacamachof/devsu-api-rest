package com.jacamachof.devsuapirest.exception;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Getter
@Component
public class ExceptionFactory {

    private final UnexpectedException unexpectedException;

    private final BusinessException clientExistsException;
    private final BusinessException clientNotFoundException;
    private final BusinessException clientInactiveException;
    private final BusinessException passwordInvalidException;

    private final BusinessException accountExistsException;
    private final BusinessException accountNotFoundException;
    private final BusinessException accountInactiveException;
    private final BusinessException accountsNotFoundException;

    private final BusinessException movementNotFoundException;
    private final BusinessException movementsNotFoundException;
    private final BusinessException statementNotFoundException;
    private final BusinessException statementInvalidRangeException;

    private final BusinessException dailyLimitException;
    private final BusinessException notBalanceException;

    public ExceptionFactory(@Autowired ResourceBundle resourceBundle) {
        this.unexpectedException = new UnexpectedException(resourceBundle.getString("error.unexpected"));

        this.clientExistsException = new BadRequestException(resourceBundle.getString("identification.alr-exist"));
        this.clientNotFoundException = new NotFoundException(resourceBundle.getString("identification.not-exist"));
        this.clientInactiveException = new BadRequestException(resourceBundle.getString("client.operation.suspended"));
        this.passwordInvalidException = new BadRequestException(resourceBundle.getString("password.not-valid"));

        this.accountExistsException = new BadRequestException(resourceBundle.getString("account-number.alr-exist"));
        this.accountNotFoundException = new NotFoundException(resourceBundle.getString("account-number.not-exist"));
        this.accountInactiveException = new BadRequestException(resourceBundle.getString("account.operation.suspended"));
        this.accountsNotFoundException = new NotFoundException(resourceBundle.getString("accounts.not-found"));

        this.movementNotFoundException = new NotFoundException(resourceBundle.getString("movement.not-found"));
        this.movementsNotFoundException = new NotFoundException(resourceBundle.getString("movements.not-found"));
        this.statementNotFoundException = new NotFoundException(resourceBundle.getString("movements.statement.not-available"));
        this.statementInvalidRangeException = new BadRequestException(resourceBundle.getString("movements.statement.invalid-range"));

        this.dailyLimitException = new BadRequestException(resourceBundle.getString("error.daily-limit"));
        this.notBalanceException = new BadRequestException(resourceBundle.getString("error.not-balance"));
    }
}
