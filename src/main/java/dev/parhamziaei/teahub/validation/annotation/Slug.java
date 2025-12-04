package dev.parhamziaei.teahub.validation.annotation;

import dev.parhamziaei.teahub.validation.validator.PhoneNumberValidator;
import dev.parhamziaei.teahub.validation.validator.SlugValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SlugValidator.class)
public @interface Slug {
    String message() default "your input is not a slug";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;
}
