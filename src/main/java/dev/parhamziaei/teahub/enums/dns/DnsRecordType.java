package dev.parhamziaei.teahub.enums.dns;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DnsRecordType {
    A,
    SRV,;

    @JsonCreator
    public static DnsRecordType fromValue(String value) {
        for (DnsRecordType type : DnsRecordType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
