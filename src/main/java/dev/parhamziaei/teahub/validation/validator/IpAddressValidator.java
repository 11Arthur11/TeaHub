package dev.parhamziaei.teahub.validation.validator;

import dev.parhamziaei.teahub.validation.annotation.IpAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IpAddressValidator implements ConstraintValidator<IpAddress, String>
{
    @Override
    public void initialize(IpAddress constraintAnnotation) {
    }

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext cvContext)
    {
        String[] parts = ip.split("\\.");

        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);

                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
