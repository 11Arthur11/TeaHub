package dev.parhamziaei.teahub.dto.response.shop.admin;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AudioBotProductListAdminResponse extends AbstractProductListResponse {
    private Long providerNodeId;
}
