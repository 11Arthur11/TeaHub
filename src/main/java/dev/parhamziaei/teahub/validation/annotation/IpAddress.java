package dev.parhamziaei.teahub.validation.annotation;

import dev.parhamziaei.teahub.validation.validator.IpAddressValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Documented
@Constraint(validatedBy = IpAddressValidator.class)
public @interface IpAddress
{
    String message() default "{ipAddress.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}