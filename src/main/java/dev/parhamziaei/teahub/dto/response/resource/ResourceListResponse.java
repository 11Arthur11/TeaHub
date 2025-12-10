package dev.parhamziaei.teahub.dto.response.resource;

import dev.parhamziaei.teahub.enums.ResourceStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceListResponse {

    private Long id;
    private String label;
    private String productName;
    private String status;
    private LocalDateTime expiration;

}
