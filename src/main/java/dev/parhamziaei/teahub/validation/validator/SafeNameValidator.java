package dev.parhamziaei.teahub.validation.validator;

import dev.parhamziaei.teahub.validation.annotation.SafeName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SafeNameValidator implements ConstraintValidator<SafeName, String> {

    private boolean allowNull;

    @Override
    public void initialize(SafeName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (allowNull && value == null) return true;
        if (value == null) return false;
        Pattern pattern = Pattern.compile("^(?!.*[!@#$%^&*+=]).{2,20}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
