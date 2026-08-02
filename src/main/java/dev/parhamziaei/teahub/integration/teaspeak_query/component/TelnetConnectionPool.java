package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.configuration.properties.TelnetProperties;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.teaspeak.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.TelnetSessionState;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryConnectionPoolingException;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryLoginFailedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionLoginFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionReviveFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionUnreachableEvent;
import dev.parhamziaei.teahub.kafka.producer.TelnetEventProducer;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private final QueryInstanceRepository queryInstanceRepository;

    TelnetConnectionPool(TelnetProperties telnetProperties, TelnetEventProducer telnetEventProducer, QueryInstanceRepository queryInstanceRepository) {
        this.timeout = telnetProperties.defaultTimeoutMillis();
        this.telnetReconnectTries = telnetProperties.reconnectTries();
        this.reconnectDelay = telnetProperties.reconnectDelay();
        this.telnetEventProducer = telnetEventProducer;
        this.queryInstanceRepository = queryInstanceRepository;
    }

    public void addConnection(ServerQueryCredentials credentials) {
        if (connections.containsKey(getKey(credentials)))
            removeConnection(credentials);

        TelnetClient client = new TelnetClient();
        client.setConnectTimeout(timeout);
        try {
            client.connect(credentials.ip(), credentials.port());
            log.debug("Pooling-Operation -> Connected to {}:{}", credentials.ip(), credentials.port());
            TelnetSession session = new TelnetSession(client, credentials);

            String key = session.getKey();
            session.login();
            session.getState().set(TelnetSessionState.IDLE);
            connections.put(key, session);
            log.debug("Pooling-Operation -> Connection session added to pool -> {}", key);
        } catch (IOException e) {
            log.error("Pooling-Operation -> Pooling failed (IOException)", e);

            TelnetSessionUnreachableEvent unreachableEvent = new TelnetSessionUnreachableEvent(credentials);
            telnetEventProducer.sendUnreachableEvent(unreachableEvent);

            throw new QueryConnectionPoolingException("error while trying to add new connection to pool: " + e.getMessage());
        } catch (QueryLoginFailedException e) {
            log.error("Pooling-Operation -> failed to login to {}:{} query",
                    credentials.ip(), credentials.port());

            TelnetSessionLoginFailedEvent event = new TelnetSessionLoginFailedEvent(credentials);
            telnetEventProducer.sendLoginFailedEvent(event);

            throw new QueryConnectionPoolingException("error while trying to add new connection to pool: " + e.getMessage());
        }
    }

    public void removeConnection(ServerQueryCredentials credentials) {
        String key = getKey(credentials);
        TelnetSession telnetSession = connections.get(key);
        if (telnetSession != null) {
            TelnetClient client = telnetSession.getClient();
            try {
                client.disconnect();
                log.info("Remove-Operation -> Client disconnected: {}", key);
                connections.remove(key);
                log.info("Remove-Operation -> Connection session removed: {}", key);
            } catch (IOException e) {
                log.error("Remove-Operation -> error while trying to remove connection: {} from the pool because: {}", key, e.getMessage());
            }
        }
    }

    public TelnetSession borrow(ServerQueryCredentials credentials) {
        int timeout = 20000;
        long start = System.currentTimeMillis();
        TelnetSession session = connections.get(getKey(credentials));
        while (System.currentTimeMillis() - start < timeout) {
            try {
                if (session.getState().compareAndSet(TelnetSessionState.IDLE, TelnetSessionState.BUSY) && session.getClient().isConnected()) {
                    session.getPoolLock().lock();
                    return session;
                } else if (session.getState().get() == TelnetSessionState.BUSY) {
                    Thread.sleep(50);
                } else {
                    changeStatusToUnreachable(credentials);
                    throw new QueryConnectionPoolingException("error while trying to borrow connection from the pool - there is a high chance that query is unreachable");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        changeStatusToUnreachable(credentials);
        throw new QueryConnectionPoolingException("timeout while trying to borrow connection from the pool");
    }

    protected void changeStatusToUnreachable(ServerQueryCredentials credentials) {
        QueryInstance queryInstance = queryInstanceRepository.findByAddress(credentials.ip(), credentials.port())
                .orElseThrow(() -> new QueryConnectionPoolingException("query instance not found"));

        queryInstance.setStatus(QueryInstanceStatus.UNREACHABLE);
        queryInstanceRepository.save(queryInstance);
    }

    public void returnToPool(TelnetSession session) {
        try {
            session.cleanInputStream();
        } catch (IOException e) {
            log.debug("Pooling-Operation -> error while trying to clean input stream: {}", e.getMessage());
        }
        session.getPoolLock().unlock();
        session.getState().set(TelnetSessionState.IDLE);
    }

    @Async
    @Scheduled(cron = "0 */1 * * * *")
    public void heartbeat() {
        connections.values()
                .stream()
                .filter(s -> s.getState().get() != TelnetSessionState.BUSY)
                .forEach(session -> {
                    log.debug("Heartbeat-Operation -> started heartbeat for: {}", session.getKey());
                    boolean connected;
                    if (session.getClient().isConnected()) {
                        String versionResponse = session.execute("version");
                        connected = versionResponse.contains("msg=ok");
                    } else {
                        connected = false;
                    }
                    if (!connected) {
                        session.getState().set(TelnetSessionState.UNHEALTHY);
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

                            TelnetSession newSession = new TelnetSession(refreshedClient, credentials);

                            newSession.login();
                            log.debug("Heartbeat-Operation -> login successful to {}:{} , adding connection to pool...", credentials.ip(), credentials.port());

                            String key = newSession.getKey();
                            connections.remove(session.getKey());
                            session.getState().set(TelnetSessionState.IDLE);
                            connections.put(key, newSession);

                            log.debug("Heartbeat-Operation -> connection added to pool -> {}", session.getKey());
                        } catch (QueryLoginFailedException e) {
                            log.error("Heartbeat-Operation -> failed to login to {}:{} with this credentials {}:{} because: {}",
                                    credentials.ip(), credentials.port(),
                                    credentials.username(), credentials.password(), e.getMessage());

                            TelnetSessionLoginFailedEvent event = new TelnetSessionLoginFailedEvent(session.getCredentials());
                            telnetEventProducer.sendLoginFailedEvent(event);
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

                TelnetSession newSession = new TelnetSession(newClient, credentials);

                newSession.login();
                log.debug("Reconnect-Operation -> login successful to {}:{} , adding connection to pool...", credentials.ip(), credentials.port());

                newSession.getState().set(TelnetSessionState.IDLE);
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

        if (!connected) {
            log.info("Reconnect-Operation -> failed to connect after {} attempts, giving up (try to reconnect by yourself)", tries);
            TelnetSessionReviveFailedEvent event = new TelnetSessionReviveFailedEvent(credentials);
            telnetEventProducer.sendReviveFailedEvent(event);
        } else {
            log.debug("Reconnect-Operation -> connection {}:{} revived successfully in {} attempts", credentials.ip(), credentials.port(), tries);
        }
    }

    public String getKey(ServerQueryCredentials credentials) {
        return credentials.ip() + ":" + credentials.port();
    }

}
