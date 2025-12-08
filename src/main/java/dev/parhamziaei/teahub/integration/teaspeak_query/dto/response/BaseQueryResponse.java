package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.Data;

@Data
public abstract class BaseQueryResponse {
    private String error;
    private String msg;
    private String id;
}
