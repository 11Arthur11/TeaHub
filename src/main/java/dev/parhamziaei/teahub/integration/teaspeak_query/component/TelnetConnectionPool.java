package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.configuration.properties.TelnetProperties;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryConnectionPoolingException;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryLoginFailedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TelnetConnectionPool {

    protected Map<String, TelnetSession> connections = new ConcurrentHashMap<>();
    protected int timeout;

    TelnetConnectionPool(TelnetProperties telnetProperties) {
        this.timeout = telnetProperties.defaultTimeoutMillis();
    }

    public void addConnection(ServerQueryCredentials credentials) throws QueryConnectionPoolingException, QueryLoginFailedException {
        TelnetClient client = new TelnetClient();
        client.setConnectTimeout(timeout);
        try {
            client.connect(credentials.ip(), credentials.port());
            log.debug("Connected to {}:{}", credentials.ip(), credentials.port());
            TelnetSession session = TelnetSession.builder()
                    .credentials(credentials)
                    .client(client)
                    .in(client.getInputStream())
                    .out(new PrintStream(client.getOutputStream()))
                    .build();

            String key = session.getKey();
            session.login();
            connections.put(key, session);
            log.debug("Connection session added to pool -> {}", key);
        } catch (Exception e) {
            throw new QueryConnectionPoolingException("error while trying to add new connection to pool: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 300000)
    private void heartbeat() {
        connections.values().forEach(session -> {
            if (!session.getClient().isConnected()) {
                TelnetClient refreshedClient = new TelnetClient();
                refreshedClient.setConnectTimeout(timeout);
                ServerQueryCredentials credentials = session.getCredentials();
                try {
                    refreshedClient.connect(
                            credentials.ip(),
                            credentials.port()
                    );
                    log.debug("HeartBeat -> successful to {}:{}", credentials.ip(), credentials.port());

                    TelnetSession newSession = TelnetSession.builder()
                            .credentials(credentials)
                            .client(refreshedClient)
                            .in(refreshedClient.getInputStream())
                            .out(new PrintStream(refreshedClient.getOutputStream()))
                            .build();

                    newSession.login();

                    String key = newSession.getKey();
                    connections.remove(session.getKey());
                    connections.put(key, newSession);

                    log.debug("HeartBeat -> added to pool -> {}", credentials.ip());
                } catch (Exception e) {
                    log.error("HeartBeat -> failed adding new connection with credentials: {}:{} - {}:{}",
                            credentials.ip(), credentials.port(), credentials.username(), credentials.password(), e);
                }
            }
        });
    }
}
