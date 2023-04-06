package com.jacamachof.devsuapirest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MovementTypeValidator.class)
public @interface MovementTypeValidation {
    String message() default "{movement-type.not-valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
