package com.jacamachof.devsuapirest.validation;

import com.jacamachof.devsuapirest.model.StateEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StateValidator implements ConstraintValidator<StateValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return Objects.nonNull(StateEnum.of(value));
    }
}
