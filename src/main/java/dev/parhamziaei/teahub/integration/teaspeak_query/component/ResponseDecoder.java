package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.configuration.properties.TelnetProperties;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.BaseQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class ResponseDecoder {

    private final static int READ_TIMEOUT = 5000;

    public static String extractRawString(InputStream in) throws IOException {
        ByteArrayOutputStream outByte = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        long endTime = System.currentTimeMillis() + READ_TIMEOUT;

        while (System.currentTimeMillis() < endTime) {
            int len = in.read(buffer);
            if (len == -1) break;

            if (len > 0) {
                outByte.write(buffer, 0, len);
                String soFar = outByte.toString(StandardCharsets.UTF_8);
                if (soFar.contains("msg=") && soFar.length() - 6 >= soFar.lastIndexOf("msg=")) break;
            }
            if (len == 0) {
                try {Thread.sleep(50);} catch (InterruptedException ignored) {}
            };
        }

        return outByte.toString(StandardCharsets.UTF_8);
    }

    private String value(String kv) {
        return kv.split("=", 2)[1];
    }

    private String key(String kv) {
        return kv.split("=", 2)[0];
    }

//    public <T extends BaseQueryResponse> T mapSinglePipeResponse(InputStream in, Class<T> mapType) throws IOException {
//        String rawOutput = extractRawString(in);
//        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        Arrays.stream(rawOutput.split(" ")).forEach(line -> {
//            if (line.contains("=")) {
//                String[] split = line.split("=", 2);
//                map.put(split[0], split[1]);
//            }
//        });
//        return modelMapper.map(map, mapType);
//    }

    public static LinkedHashMap<String, String> convertToMap(String rawResponse) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Arrays.stream(rawResponse.replace("error","")
                .split(" "))
                .forEach(line -> {
                    if (line.contains("=")) {
                        String[] split = line.split("=", 2);
                        map.put(split[0].trim(), split[1].trim());
                    }
                });
        return map;
    }

    public static List<LinkedHashMap<String, String>> convertMultiPipeToMap(String rawResponse) {
        List<LinkedHashMap<String, String>> response = new ArrayList<>();
        String[] pipes = rawResponse.split("\\|");
        for (String pipe : pipes){
            response.add(convertToMap(pipe));
        }
        return response;
    }

//    public static List<LinkedHashMap<String, String>> extractResponseRecords(InputStream in, long timeoutMillis) throws IOException {
//        LinkedHashMap<String, String> response = new LinkedHashMap<>();
//        StringBuilder sb = new StringBuilder();
//        byte[] buffer = new byte[1024];
//        long endTime = System.currentTimeMillis() + timeoutMillis;
//
//        while (System.currentTimeMillis() < endTime) {
//            if (in.available() > 0) {
//                int len = in.read(buffer);
//                if (len == -1) break;
//                sb.append(new String(buffer, 0, len));
//                if (sb.toString().contains("msg=")) break;
//            } else {
//                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
//            }
//        }
//
//        String[] parts = sb.toString().split(" ");
//        for (String part : parts) {
//            if (part.contains("=")) {
//                String[] kv = part.split("=", 2);
//                response.put(kv[0], kv[1]);
//            }
//        }
//        return response;
//    }


}
