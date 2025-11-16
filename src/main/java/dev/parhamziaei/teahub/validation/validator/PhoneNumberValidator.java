package dev.parhamziaei.teahub.validation.validator;

import dev.parhamziaei.teahub.validation.annotation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {

    }

    // note: example number: 0912-345-1183
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) return false;

        Pattern pattern = Pattern.compile("^09[0-9]{9}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
