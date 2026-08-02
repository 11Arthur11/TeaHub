package dev.parhamziaei.teahub.validation.validator;

import dev.parhamziaei.teahub.validation.annotation.Slug;
import dev.parhamziaei.teahub.validation.annotation.Subdomain;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubdomainValidator implements ConstraintValidator<Subdomain, String> {

    private boolean allowNull;

    @Override
    public void initialize(Subdomain constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String slug, ConstraintValidatorContext constraintValidatorContext) {
        if (slug == null && allowNull) {
            return true;
        }
        if (slug == null) return false;
        Pattern pattern = Pattern.compile("^(?=.{1,63}$)(?!-)[a-z0-9]+(?:-[a-z0-9]+)*(?<!-)$");
        Matcher matcher = pattern.matcher(slug);
        return matcher.matches();
    }
}
