package dev.parhamziaei.teahub.integration.ippanel.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class PatternMessageRequest {
    private final String sending_type;
    private String from_number;
    private String code;
    private List<String> recipients;
    private Map<String, String> params;
}
