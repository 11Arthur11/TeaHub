package dev.parhamziaei.teahub.validation;

import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import dev.parhamziaei.teahub.validation.validator.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "0.0.0.0", "255.255.255.255", "192.168.1.10"})
    void acceptsValidIpv4Addresses(String value) {
        assertTrue(new IpAddressValidator().isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "127.0.0", "127.0.0.256", "127.0.a.1", "-1.0.0.0"})
    void rejectsInvalidIpv4Addresses(String value) {
        assertFalse(new IpAddressValidator().isValid(value, null));
    }

    @Test
    void rejectsNullIpv4Address() {
        assertFalse(new IpAddressValidator().isValid(null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"StrongP4ss!", "TeaHub#2026", "Abcdef1+"})
    void acceptsStrongPasswords(String value) {
        assertTrue(new PasswordValidator().isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"password1!", "PASSWORD1!", "Password!!", "Pass word1!", "Short1!"})
    void rejectsPasswordsMissingRequiredCharacterClasses(String value) {
        assertFalse(new PasswordValidator().isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"9123456789", "9000000000"})
    void acceptsIranianMobileNumberBody(String value) {
        assertTrue(new PhoneNumberValidator().isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"09123456789", "+989123456789", "8123456789", "91234", ""})
    void rejectsInvalidMobileNumberBody(String value) {
        assertFalse(new PhoneNumberValidator().isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"tea-cloud", "service1", "a-b-c"})
    void acceptsSlugsAndSubdomains(String value) {
        assertTrue(new SlugValidator().isValid(value, null));
        assertTrue(new SubdomainValidator().isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TeaCloud", "-service", "service-", "service_name", "service..name", ""})
    void rejectsInvalidSlugsAndSubdomains(String value) {
        assertFalse(new SlugValidator().isValid(value, null));
        assertFalse(new SubdomainValidator().isValid(value, null));
    }

    @Test
    void validatesEnumThroughConfiguredGetter() throws NoSuchFieldException {
        EnumValue annotation = EnumHolder.class.getDeclaredField("type").getAnnotation(EnumValue.class);
        EnumValueValidator validator = new EnumValueValidator();
        validator.initialize(annotation);

        assertTrue(validator.isValid("TEASPEAK", null));
        assertTrue(validator.isValid("AUDIO_BOT", null));
        assertFalse(validator.isValid("UNKNOWN", null));
        assertFalse(validator.isValid(null, null));
    }

    private static class EnumHolder {
        @EnumValue(enumClass = ResourceType.class, enumValueGetter = "name")
        private String type;
    }
}
