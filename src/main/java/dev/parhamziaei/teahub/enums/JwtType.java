package dev.parhamziaei.teahub.enums;

public enum JwtType {
    ACCESS_TOKEN("ACCESS_TOKEN"),
    REFRESH_TOKEN("REFRESH_TOKEN"),
    TWO_FACTOR_TOKEN("TWO_FACTOR_TOKEN"),
    PHONE_VERIFY_TOKEN("PHONE_VERIFY_TOKEN");

    JwtType(String value) {
        this.value =  value;
    }
    private final String value;
    public String value(){
        return this.value;
    }
}
