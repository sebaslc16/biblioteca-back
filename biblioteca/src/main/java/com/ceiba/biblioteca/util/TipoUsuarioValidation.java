package com.ceiba.biblioteca.util;

import com.ceiba.biblioteca.validator.TipoUsuarioValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoUsuarioValidator.class)
public @interface TipoUsuarioValidation {
    String message() default "Tipo de usuario no permitido en la biblioteca";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
