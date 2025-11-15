package dev.parhamziaei.teahub.validation.annotation;

import dev.parhamziaei.teahub.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PhoneNumber {
    String message() default "phone number is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
