package dev.parhamziaei.teahub.integration;

import dev.parhamziaei.teahub.integration.teaspeak_query.component.QueryCommandFactory;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.ResponseDecoder;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TeaSpeakProtocolTest {

    private final QueryCommandFactory commands = new QueryCommandFactory();

    @Test
    void buildsServerLifecycleCommandsUsingTeaSpeakProtocol() {
        TSCreateQueryRequest request = TSCreateQueryRequest.builder()
                .serverName("TeaHub\\sResource")
                .port("9987")
                .maxClients("32")
                .build();

        assertEquals(
                "servercreate virtualserver_name=TeaHub\\sResource virtualserver_port=9987 virtualserver_maxclients=32",
                commands.createServerCommand(request)
        );
        assertEquals("serverstart sid=12", commands.startCommand("12"));
        assertEquals("serverstop sid=12", commands.stopCommand("12"));
        assertEquals("serverdelete sid=12", commands.deleteServerCommand("12"));
        assertEquals("use sid=12", commands.useCommand("12"));
    }

    @Test
    void buildsPrivilegeCommands() {
        assertEquals(
                "privilegekeyadd tokentype=0 tokenid1=6 tokenid2=0 tokendescription=Generated\\sBy\\sTeaHub",
                commands.generatePrivilegeCommand("6")
        );
        assertEquals("privilegekeylist", commands.privilegeListCommand());
        assertEquals("privilegekeydelete token=secret-token", commands.deletePrivilegeCommand("secret-token"));
    }

    @Test
    void decodesSingleAndMultiRecordResponses() {
        Map<String, String> single = ResponseDecoder.convertToMap("sid=4 virtualserver_name=TeaHub error id=0 msg=ok");
        List<? extends Map<String, String>> multiple = ResponseDecoder.convertMultiPipeToMap(
                "sid=1 virtualserver_name=One|sid=2 virtualserver_name=Two error id=0 msg=ok"
        );

        assertEquals("4", single.get("sid"));
        assertEquals("TeaHub", single.get("virtualserver_name"));
        assertEquals("0", single.get("id"));
        assertEquals(2, multiple.size());
        assertEquals("1", multiple.get(0).get("sid"));
        assertEquals("2", multiple.get(1).get("sid"));
    }

    @Test
    void readsProtocolResponseUntilErrorMessage() throws Exception {
        String raw = "sid=4 virtualserver_name=TeaHub error id=0 msg=ok";

        String result = ResponseDecoder.extractRawString(
                new ByteArrayInputStream(raw.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(raw, result);
    }
}
