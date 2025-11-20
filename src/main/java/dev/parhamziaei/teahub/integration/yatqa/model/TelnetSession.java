package dev.parhamziaei.teahub.integration.yatqa.model;

import dev.parhamziaei.teahub.integration.yatqa.exception.YatqaLoginFailedException;
import dev.parhamziaei.teahub.integration.yatqa.utils.ReadUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

@Slf4j
@Builder
@Data
public class TelnetSession {

    private TelnetClient client;
    private ServerQueryCredentials credentials;
    private PrintStream out;
    private InputStream in;

    public void login() {
        out.println("login " + credentials.username() + " " + credentials.password());
        out.flush();
        try {
            Map<String, String> response = ReadUtil.extractRawResponse(in, 5000);
            String responseId = response.get("id");
            String responseMsg = response.get("msg");
            if (!responseId.equals("0") && !responseMsg.equals("ok")) {
                throw new YatqaLoginFailedException(
                        "login to "
                                + credentials.ip()
                                + " failed with credentials: "
                                + credentials.username() + ":" + credentials.password());
            }

            log.info("login successful to {}", credentials.ip());
        } catch (IOException e) {
            log.error("failed login to {}", credentials.ip());
        }
    }

    public void sendCommand(String command) {
        if (client.isConnected()) {
            out.println(command);
            out.flush();
            try {
                Map<String, String> rawResponse = ReadUtil.extractRawResponse(in, 10000);
                String responseId = rawResponse.get("id");
                String responseMessage = rawResponse.get("msg");
                if (responseId.equals("0") && responseMessage.equals("ok")) {
                    log.info("command sent");
                }
            } catch (IOException e) {
                log.error("failed to send command to {}", credentials.ip());
            }
        }
    }

}
