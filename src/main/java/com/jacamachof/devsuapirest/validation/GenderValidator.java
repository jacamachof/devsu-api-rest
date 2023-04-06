package com.jacamachof.devsuapirest.validation;

import com.jacamachof.devsuapirest.model.GenderEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class GenderValidator implements ConstraintValidator<GenderValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return Objects.nonNull(GenderEnum.of(value));
    }
}
