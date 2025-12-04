package dev.parhamziaei.teahub.validation.validator;

import dev.parhamziaei.teahub.validation.annotation.Slug;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlugValidator implements ConstraintValidator<Slug, String> {

    private boolean allowNull;

    @Override
    public void initialize(Slug constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String slug, ConstraintValidatorContext constraintValidatorContext) {
        if (slug == null && allowNull) {
            return true;
        }
        if (slug == null) return false;
        Pattern pattern = Pattern.compile("^[a-z]{2,10}$");
        Matcher matcher = pattern.matcher(slug);
        return matcher.matches();
    }
}
