package dev.parhamziaei.teahub.utils;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumbers {

    private PhoneNumbers() {}

    public static String formatedOf(String phoneNumber) {
        if (isFormatted.test(phoneNumber)) {
            return phoneNumber;
        }
        return phoneNumber.replaceFirst("^0", "+98");
    }

    public static String normalizeOf(String phoneNumber) {
        return phoneNumber.replaceFirst("^\\+98", "0");
    }

    public static Predicate<String> isFormatted = (String phoneNumber) -> {
        Pattern pattern = Pattern.compile("^\\+98[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    };

}
