package dev.parhamziaei.teahub.integration.ippanel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class IPPanelResponseMeta {
    private boolean status;
    private String message;
    private List<String> message_parameters;
    private String message_code;

    public boolean isOk() {
        return status;
    }
}
