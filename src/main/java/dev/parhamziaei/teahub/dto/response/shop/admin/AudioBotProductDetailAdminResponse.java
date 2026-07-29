package dev.parhamziaei.teahub.dto.response.shop.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "AudioBotProductDetailAdminResponse")
public class AudioBotProductDetailAdminResponse extends AbstractProductDetailResponse {
    private Long providerNodeId;
}
