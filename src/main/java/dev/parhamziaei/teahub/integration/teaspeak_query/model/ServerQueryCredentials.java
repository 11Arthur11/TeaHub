package dev.parhamziaei.teahub.integration.teaspeak_query.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record ServerQueryCredentials(
        String ip,
        int port,
        String username,
        String password
) {

    public ServerQueryCredentials(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

}
