package dev.parhamziaei.teahub.dto.response.resource;

import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractResourceDetailResponse {

    private Long id;
    private String label;
    private String productName;
    private ResourceType resourceType;
    private ResourceStatus resourceStatus;
    private ProductPeriod period;
    private LocalDateTime orderDate;
    private LocalDateTime expiration;
    private boolean autoProlong;

}
