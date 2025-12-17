package dev.parhamziaei.teahub.enums.user;

import java.util.Set;

public enum Roles {

    USER(0, "ROLE_USER", "authority.user"),
    SUPPORT(1, "ROLE_SUPPORT", "authority.support"),
    ADMIN(2, "ROLE_ADMIN",  "authority.admin"),;

    private final int hierarchy;
    private final String value;
    private final String key;

    Roles(int order, String name, String key) {
        this.hierarchy = order;
        this.value = name;
        this.key = key;
    }

    public String nameWithoutPrefix() {
        return this.value.replaceFirst("ROLE_", "");
    }

    public String value(){
        return this.value;
    }

    public String key() {
        return this.key;
    }

    public static Roles fromName(String name) {
        return Roles.valueOf(name.replaceFirst("ROLE_", ""));
    }
    
    public int hierarchy() {
        return this.hierarchy;
    }

    public static Set<Roles> staffRoles() {
        return Set.of(ADMIN, SUPPORT);
    }
}
