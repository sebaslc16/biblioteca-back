package com.ceiba.biblioteca.validator;

import com.ceiba.biblioteca.util.TipoUsuarioValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TipoUsuarioValidator implements ConstraintValidator<TipoUsuarioValidation, Integer> {


    @Override
    public void initialize(TipoUsuarioValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == 1 || value == 2 || value == 3;
    }
}
