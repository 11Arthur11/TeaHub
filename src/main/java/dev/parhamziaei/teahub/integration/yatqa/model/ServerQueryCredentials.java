package dev.parhamziaei.teahub.integration.yatqa.model;

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
