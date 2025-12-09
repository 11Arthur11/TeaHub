package dev.parhamziaei.teahub.dto.response.resource;

import dev.parhamziaei.teahub.enums.ResourceStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseResourceDetailResponse {

    private Long id;
    private String label;
    private String productName;
    private ResourceStatus resourceStatus;
    private LocalDateTime orderDate;
    private LocalDateTime expiration;
    private boolean autoProlong;

}
