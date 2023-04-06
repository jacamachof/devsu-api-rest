package com.jacamachof.devsuapirest.validation;

import com.jacamachof.devsuapirest.model.AccountType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AccountTypeValidator implements ConstraintValidator<AccountTypeValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return Objects.nonNull(AccountType.of(value));
    }
}
