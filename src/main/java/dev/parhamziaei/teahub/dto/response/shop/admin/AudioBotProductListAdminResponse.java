package dev.parhamziaei.teahub.dto.response.shop.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AudioBotProductListAdminResponse extends AbstractProductListAdminResponse {
    private Long providerNodeId;
    private boolean enabled;
}
