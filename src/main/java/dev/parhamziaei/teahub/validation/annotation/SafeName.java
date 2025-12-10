package dev.parhamziaei.teahub.validation.annotation;

import dev.parhamziaei.teahub.validation.validator.SafeNameValidator;
import dev.parhamziaei.teahub.validation.validator.SlugValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SafeNameValidator.class)
public @interface SafeName {
    String message() default "input name is not acceptable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;
}