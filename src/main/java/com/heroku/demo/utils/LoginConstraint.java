package com.heroku.demo.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginConstraint {
    String message() default "Invalid login";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}