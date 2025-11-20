package dev.parhamziaei.teahub.integration.yatqa;

import dev.parhamziaei.teahub.integration.yatqa.dto.request.VMCreateRequest;
import org.springframework.stereotype.Service;

import static dev.parhamziaei.teahub.integration.yatqa.enums.TSQuery.*;

@Service
public class YatqaCommandFactory {



    public String createServerCommand(VMCreateRequest req) {
        return CREATE_SERVER.cmd()
                + " virtualserver_name=" + req.getServerName()
                + " virtualserver_port=" + req.getPort()
                + " virtualserver_maxclients=" + req.getMaxClients();
    }

}
