package dev.parhamziaei.teahub.dto.response.resource;

import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractResourceDetailResponse {

    private Long id;
    private String label;
    private String productName;
    private ResourceType resourceType;
    private String resourceStatus;
    private LocalDateTime orderDate;
    private LocalDateTime expiration;
    private boolean autoProlong;

}
