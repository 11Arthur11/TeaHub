package dev.parhamziaei.teahub.validation.validator;

import dev.parhamziaei.teahub.validation.annotation.PasswordValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if (value.length() < 8 || value.length() > 20) {
            return false;
        }
        if (value.contains(" ")) {
            return false;
        }
        String regexPn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regexPn);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}
