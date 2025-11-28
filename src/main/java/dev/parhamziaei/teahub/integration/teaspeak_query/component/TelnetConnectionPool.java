package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.configuration.properties.TelnetProperties;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryConnectionPoolingException;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryLoginFailedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionUnreachableEvent;
import dev.parhamziaei.teahub.kafka.producer.TelnetEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TelnetConnectionPool {

    protected Map<String, TelnetSession> connections = new ConcurrentHashMap<>();
    protected int timeout;
    protected int telnetReconnectTries;
    protected Duration reconnectDelay;
    private final TelnetEventProducer telnetEventProducer;

    TelnetConnectionPool(TelnetProperties telnetProperties, TelnetEventProducer telnetEventProducer) {
        this.timeout = telnetProperties.defaultTimeoutMillis();
        this.telnetReconnectTries = telnetProperties.reconnectTries();
        this.reconnectDelay = telnetProperties.reconnectDelay();
        this.telnetEventProducer = telnetEventProducer;
    }

    public void addConnection(ServerQueryCredentials credentials) {
        TelnetClient client = new TelnetClient();
        client.setConnectTimeout(timeout);
        try {
            client.connect(credentials.ip(), credentials.port());
            log.debug("Pooling-Operation -> Connected to {}:{}", credentials.ip(), credentials.port());
            TelnetSession session = TelnetSession.builder()
                    .credentials(credentials)
                    .client(client)
                    .in(client.getInputStream())
                    .out(new PrintStream(client.getOutputStream()))
                    .build();

            String key = session.getKey();
            session.login();
            connections.put(key, session);
            log.debug("Pooling-Operation -> Connection session added to pool -> {}", key);
        } catch (IOException e) {
            throw new QueryConnectionPoolingException("error while trying to add new connection to pool: " + e.getMessage());
        } catch (QueryLoginFailedException e) {
            log.error("Pooling-Operation -> failed to login to {}:{} with this credentials {}:{} because: {}",
                    credentials.ip(), credentials.port(),
                    credentials.username(), credentials.password(), e.getMessage());

            throw new QueryConnectionPoolingException("error while trying to add new connection to pool: " + e.getMessage());

            //note: produce new event
        }
    }

    public void removeConnection(ServerQueryCredentials credentials) {
        String key = credentials.ip() + ":" + credentials.port();
        TelnetSession telnetSession = connections.get(key);
        if (telnetSession != null) {
            TelnetClient client = telnetSession.getClient();
            try {
                client.disconnect();
                log.info("Remove-Operation -> Client disconnected: {}", key);
                connections.remove(key);
                log.info("Remove-Operation -> Connection session removed: {}", key);
            } catch (IOException e) {
                log.error("Remove-Operation -> error while trying to remove connection: {} from the pool: {}", key, e.getMessage());
            }
        }
    }

    @Async
    @Scheduled(fixedRate = 300000)
    public void heartbeat() {
        log.debug("Heartbeat-Operation -> started...");
        connections.values().forEach(session -> {
            if (!session.getClient().isConnected()) {
                log.debug("Heartbeat-Operation -> new dead connection detected trying to heartbeat...");

                TelnetClient refreshedClient = new TelnetClient();
                refreshedClient.setConnectTimeout(timeout);
                ServerQueryCredentials credentials = session.getCredentials();
                try {
                    session.getClient().disconnect();
                    refreshedClient.connect(
                            credentials.ip(),
                            credentials.port()
                    );
                    log.debug("Heartbeat-Operation -> successful to {}:{} , trying to login...", credentials.ip(), credentials.port());

                    TelnetSession newSession = TelnetSession.builder()
                            .credentials(credentials)
                            .client(refreshedClient)
                            .in(refreshedClient.getInputStream())
                            .out(new PrintStream(refreshedClient.getOutputStream()))
                            .build();

                    newSession.login();
                    log.debug("Heartbeat-Operation -> login successful to {}:{} , adding connection to pool...", credentials.ip(), credentials.port());

                    String key = newSession.getKey();
                    connections.remove(session.getKey());
                    connections.put(key, newSession);

                    log.debug("Heartbeat-Operation -> connection added to pool -> {}", session.getKey());
                } catch (QueryLoginFailedException e) {
                    log.error("Heartbeat-Operation -> failed to login to {}:{} with this credentials {}:{} because: {}",
                            credentials.ip(), credentials.port(),
                            credentials.username(), credentials.password(), e.getMessage());

                    //note: produce new event
                } catch (IOException e) {
                    log.error("Heartbeat-Operation -> failed to send heartbeat and adding new connection: {}:{} (IOException)",
                            credentials.ip(), credentials.port(), e);

                    TelnetSessionUnreachableEvent unreachableEvent = new TelnetSessionUnreachableEvent(credentials);
                    telnetEventProducer.sendUnreachableEvent(unreachableEvent);
                    log.debug("Heartbeat-Operation -> Telnet Login-Failed event produced for: {}:{}",
                            credentials.ip(), credentials.port());
                }
            }
        });
    }

    @Async
    public void reLogin(TelnetSession session) {

    }

    @Async
    public void reconnect(ServerQueryCredentials credentials) {
        int tries = 0;
        boolean connected = false;
        TelnetClient newClient = new TelnetClient();
        newClient.setConnectTimeout(timeout);
        while (tries++ < telnetReconnectTries) {
            try {
                TelnetSession oldSession = connections.get(TelnetSession.buildKey(credentials));
                if (oldSession != null) {
                    oldSession.getClient().disconnect();
                }

                log.debug("Reconnect-Operation -> trying to reconnect to {}:{} , attempts: {}", credentials.ip(), credentials.port(), tries);
                newClient.connect(
                        credentials.ip(),
                        credentials.port()
                );
                log.debug("Reconnect-Operation -> successful to {}:{} , trying to login...", credentials.ip(), credentials.port());

                TelnetSession newSession = TelnetSession.builder()
                        .credentials(credentials)
                        .client(newClient)
                        .in(newClient.getInputStream())
                        .out(new PrintStream(newClient.getOutputStream()))
                        .build();

                newSession.login();
                log.debug("Reconnect-Operation -> login successful to {}:{} , adding connection to pool...", credentials.ip(), credentials.port());

                connections.remove(newSession.getKey());
                connections.put(newSession.getKey(), newSession);
                log.debug("Reconnect-Operation -> connection added to pool -> {} , attempts: {}", newSession.getKey(), tries);
                connected = true;
                break;
            } catch (QueryLoginFailedException e) {
                log.error("Reconnect-Operation -> login failed while reconnecting to {}:{} - credentials: {}:{}",
                        credentials.ip(), credentials.port(), credentials.username(), credentials.password(), e);
            } catch (IOException e) {
                log.error("Reconnect-Operation -> failed to reconnect to {}:{} (IOException)",
                        credentials.ip(), credentials.port(), e);
            }
            try {Thread.sleep(reconnectDelay);} catch (InterruptedException ignored) {}
        }

        if (!connected)
            log.info("Reconnect-Operation -> failed to connect after {} attempts, giving up (try to reconnect by yourself)", tries);
        else
            log.debug("Reconnect-Operation -> connection {}:{} revived successfully in {} attempts", credentials.ip(), credentials.port(), tries);
    }

}
