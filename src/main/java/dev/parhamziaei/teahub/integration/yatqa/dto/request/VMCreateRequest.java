package dev.parhamziaei.teahub.integration.yatqa.dto.request;

import lombok.Data;

@Data
public class VMCreateRequest {

    String serverName;
    String port;
    String maxClients;

}
