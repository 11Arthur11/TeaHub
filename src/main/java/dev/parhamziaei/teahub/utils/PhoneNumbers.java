package dev.parhamziaei.teahub.utils;

public class PhoneNumbers {

    private PhoneNumbers() {}

    public static String formatedOf(String phoneNumber) {
        return phoneNumber.replaceFirst("^0", "+98");
    }

    private static String normalizeOf(String phoneNumber) {
        return phoneNumber.replaceFirst("^\\+98", "0");
    }
}
