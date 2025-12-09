package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.parhamziaei.teahub.integration.teaspeak_query.enums.TSQuery.*;

@Component
public class QueryCommandFactory {

    public String createServerCommand(TSCreateQueryRequest req) {
        return CREATE_SERVER.cmd()
                + " virtualserver_name=" + req.getServerName()
                + " virtualserver_port=" + req.getPort()
                + " virtualserver_maxclients=" + req.getMaxClients();
    }

    public String useCommand(String sid) {
        return USE.cmd()
                + " sid=" + sid;
    }

    public String generatePrivilegeCommand(String serverGroupId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTime = LocalDateTime.now().format(dtf);
        return PRIVILEGE_KEY_ADD.cmd()
                + " tokentype=0"
                + " tokenid1=" + serverGroupId
                + " tokenid2=0"
                + " tokendescription=" + "Generated\\sBy\\sTeaHub\\sAt:\\s" + dateTime;
    }

}
