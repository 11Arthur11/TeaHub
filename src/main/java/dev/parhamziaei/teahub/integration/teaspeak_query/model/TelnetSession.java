package dev.parhamziaei.teahub.integration.teaspeak_query.model;

import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryLoginFailedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.ResponseDecoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

@Slf4j
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelnetSession {

    private TelnetClient client;
    private ServerQueryCredentials credentials;
    private PrintStream out;
    private InputStream in;

    public String getKey() {
        return credentials.ip() + ":" + credentials.port();
    }

    public void login() {
        out.println("login " + credentials.username() + " " + credentials.password());
        out.flush();
        try {
            String response = ResponseDecoder.extractRawString(in);
            if (response != null && !response.contains("msg=ok")) {
                throw new QueryLoginFailedException(
                        "login to "
                                + credentials.ip()
                                + " failed with credentials: "
                                + credentials.username() + ":" + credentials.password());
            }

            log.info("login successful to {}:{}", credentials.ip(), credentials.port());
        } catch (IOException e) {
            throw new QueryLoginFailedException(
                    "unexpected error while login to " + credentials.ip() + ":" + credentials.port()
            );
        }
    }

    public static String buildKey(ServerQueryCredentials credentials) {
        return credentials.ip() + ":" + credentials.port();
    }

}
