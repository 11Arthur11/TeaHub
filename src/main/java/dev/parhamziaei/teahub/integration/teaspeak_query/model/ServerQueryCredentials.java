package dev.parhamziaei.teahub.integration.teaspeak_query.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ServerQueryCredentials(

        @Column(nullable = false)
        String ip,

        @Column(nullable = false)
        int port,

        @Column(nullable = false)
        String username,

        @Column(nullable = false)
        String password

) {

    public ServerQueryCredentials(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

}
