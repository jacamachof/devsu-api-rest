package com.jacamachof.devsuapirest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StateValidator.class)
public @interface StateValidation {
    String message() default "{state.not-valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
