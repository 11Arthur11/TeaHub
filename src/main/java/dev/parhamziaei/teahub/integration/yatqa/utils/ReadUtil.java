package dev.parhamziaei.teahub.integration.yatqa.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReadUtil {

    public static Map<String, String> extractRawResponse(InputStream in, long timeoutMillis) throws IOException {
        Map<String, String> response = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        long endTime = System.currentTimeMillis() + timeoutMillis;

        while (System.currentTimeMillis() < endTime) {
            if (in.available() > 0) {
                int len = in.read(buffer);
                if (len == -1) break;
                sb.append(new String(buffer, 0, len));
                if (sb.toString().contains("msg=")) break;
            } else {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        }

        String[] parts = sb.toString().split(" ");
        for (String part : parts) {
            if (part.contains("=")) {
                String[] kv = part.split("=", 2);
                response.put(kv[0], kv[1]);
            }
        }

        return response;
    }


}
