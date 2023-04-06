package com.jacamachof.devsuapirest.validation;

import com.jacamachof.devsuapirest.model.MovementType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MovementTypeValidator implements ConstraintValidator<MovementTypeValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return Objects.nonNull(MovementType.of(value));
    }
}
