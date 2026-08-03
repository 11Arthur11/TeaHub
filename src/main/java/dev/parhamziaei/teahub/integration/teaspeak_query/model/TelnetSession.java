package dev.parhamziaei.teahub.integration.teaspeak_query.model;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.TelnetSessionState;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryCommandExecutionException;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryLoginFailedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.ResponseDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class TelnetSession {

    @Getter
    private TelnetClient client;

    @Getter
    private ServerQueryCredentials credentials;

    @Getter
    private PrintStream out;

    @Getter
    private InputStream in;

    private final ReentrantLock poolLock = new ReentrantLock();

    @Getter
    private final Semaphore lock = new Semaphore(1);

    @Getter
    private final AtomicReference<TelnetSessionState> state = new AtomicReference<>();

    public TelnetSession(TelnetClient client, ServerQueryCredentials credentials) {
        this.client = client;
        this.credentials = credentials;
        this.out = new PrintStream(client.getOutputStream());
        this.in = client.getInputStream();
    }

    public void lock() {
        this.poolLock.lock();
        log.debug("Telnet Session ({}:{}) locked.", credentials.ip(), credentials.port());
    }

    public void unlock() {
        this.poolLock.unlock();
        log.debug("Telnet Session ({}:{}) unlocked.", credentials.ip(), credentials.port());
    }

    public String getKey() {
        return credentials.ip() + ":" + credentials.port();
    }

    public void login() {
        String loginCommand = "login client_login_name=" + credentials.username() + " client_login_password=" + credentials.password();
        String response = execute(loginCommand);
        if (response != null && !response.contains("msg=ok")) {
            throw new QueryLoginFailedException(
                    "login to "
                    + getKey()
                    + " failed with credentials: "
                    + credentials.username() + ":" + credentials.password()
            );
        }
        log.info("Telnet Query -> login successful to {}:{}", credentials.ip(), credentials.port());
    }

    public String execute(String command) {
        boolean acquired = false;
        try {
            this.lock.acquire();
            acquired = true;
            out.println(command);
            out.flush();
            log.debug("Telnet Query -> command: [{}] executed to ({})", command, getKey());
            String response = ResponseDecoder.extractRawString(in);
            if (response.contains("msg=ok")) {
                return response;
            } else {
                log.warn("Telnet Query -> send command failed to {}:{} - command: [{}] response -> {}", credentials.ip(), credentials.port(), command, response);
                throw new QueryCommandExecutionException("send command failed to " + credentials.ip() + ":" + credentials.port());
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Telnet Query -> unexpected error while executing ({}) to {}:{}",  command, credentials.ip(), credentials.port(), e);
            throw new QueryCommandExecutionException("unexpected error while executing command: " + command);
        } finally {
            if (acquired)
                this.lock.release();
        }
    }

//    public void sendCommandAndVerify(String command) {
//        String rawResponse = sendCommand(command);
//        if (rawResponse != null && !rawResponse.contains("msg=ok")) {
//            log.warn("send command failed to {}:{} - command: -{} response -> {}", credentials.ip(), credentials.port(), command, rawResponse);
//            throw new QueryCommandExecutionException("send command failed to " + credentials.ip() + ":" + credentials.port());
//        }
//    }

    public static String buildKey(ServerQueryCredentials credentials) {
        return credentials.ip() + ":" + credentials.port();
    }

    public void cleanInputStream() throws IOException {
        while (in.available() > 0) {
            in.read();
        }
    }

}
