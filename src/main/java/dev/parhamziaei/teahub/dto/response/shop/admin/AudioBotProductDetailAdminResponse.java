package dev.parhamziaei.teahub.dto.response.shop.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AudioBotProductDetailAdminResponse extends AbstractProductDetailResponse {
    private Long providerNodeId;
}
