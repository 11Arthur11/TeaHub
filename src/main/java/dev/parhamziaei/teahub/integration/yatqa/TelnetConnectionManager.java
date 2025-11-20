package dev.parhamziaei.teahub.integration.yatqa;

import dev.parhamziaei.teahub.integration.yatqa.exception.YatqaLoginFailedException;
import dev.parhamziaei.teahub.integration.yatqa.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.yatqa.model.TelnetSession;
import dev.parhamziaei.teahub.integration.yatqa.utils.ReadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelnetConnectionManager {

    protected Map<String, TelnetSession> connections = new ConcurrentHashMap<>();

    public void addConnection(ServerQueryCredentials credentials) {
        TelnetClient client = new TelnetClient();
        client.setConnectTimeout(5000);
        try {
            client.connect(credentials.ip(), credentials.port());
            log.info("Connected to {}:{}", credentials.ip(), credentials.port());
            TelnetSession session = TelnetSession.builder()
                    .credentials(credentials)
                    .client(client)
                    .in(client.getInputStream())
                    .out(new PrintStream(client.getOutputStream()))
                    .build();

            session.login();
            connections.put(credentials.ip(), session);
            log.info("Connection session added to pool -> {}", credentials.ip());
        } catch (Exception e) {
            log.error("failed adding new connection with credentials: {}:{} - {}:{}", credentials.ip(), credentials.port(), credentials.username(), credentials.password(), e);
        }
    }




}
