package dev.parhamziaei.teahub.dto.response.resource;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceListResponse {

    private Long id;
    private String label;
    private String productName;
    private String resourceStatus;
    private LocalDateTime expiration;

}
