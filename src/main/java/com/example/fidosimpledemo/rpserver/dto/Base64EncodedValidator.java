package com.example.fidosimpledemo.rpserver.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;

public class Base64EncodedValidator implements ConstraintValidator<Base64Encoded, String>  {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Base64.getUrlDecoder().decode(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
