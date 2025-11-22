package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import lombok.Data;

@Data
public class QueryInstanceInitRequest {

    private String yatqaIp;
    private Integer yatqaPort;
    private String yatqaUsername;
    private String yatqaPassword;
    private Integer maxVM;
    private Integer startPort;
    private Integer endPort;
    private boolean enabled;

}
