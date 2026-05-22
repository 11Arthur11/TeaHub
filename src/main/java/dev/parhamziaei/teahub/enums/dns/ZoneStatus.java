package dev.parhamziaei.teahub.enums.dns;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ZoneStatus {
    CREATING("zone-status.creating"),
    PENDING("zone-status.pending"),
    ACTIVE("zone-status.active"),
    DELETING("zone-status.deleting"),;

    private final String key;

    ZoneStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    @JsonCreator
    public static ZoneStatus fromValue(String value) {
        for (ZoneStatus status : ZoneStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }


}
