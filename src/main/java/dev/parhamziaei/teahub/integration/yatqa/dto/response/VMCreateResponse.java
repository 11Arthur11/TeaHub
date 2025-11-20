package dev.parhamziaei.teahub.integration.yatqa.dto.response;

import lombok.Data;

@Data
public class VMCreateResponse {
    String virtualserver_name;
    String virtualserver_port;
    String virtualserver_maxclients;
    String sid;
    String token;
}
