package com.example.fidosimpledemo.rpserver.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Base64EncodedValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64Encoded {
    String message() default "must be a well-formed base64";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
