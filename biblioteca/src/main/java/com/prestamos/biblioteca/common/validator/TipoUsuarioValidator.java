package com.prestamos.biblioteca.common.validator;

import com.prestamos.biblioteca.common.util.validation.TipoUsuarioValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TipoUsuarioValidator implements ConstraintValidator<TipoUsuarioValidation, Integer> {

    @Override
    public void initialize(TipoUsuarioValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        } else {
            return value == 1 || value == 2 || value == 3;
        }

    }
}
