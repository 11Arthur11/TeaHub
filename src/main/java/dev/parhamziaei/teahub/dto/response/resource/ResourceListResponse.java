package dev.parhamziaei.teahub.dto.response.resource;

import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceListResponse {

    private Long id;
    private String label;
    private String productName;
    private ResourceStatus resourceStatus;
    private ResourceType resourceType;
    private LocalDateTime expiration;

}
