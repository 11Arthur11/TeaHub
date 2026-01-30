package dev.parhamziaei.teahub.dto.request.shop.admin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TeaSpeakProductInitRequest.class, name = "TEASPEAK"),
        @JsonSubTypes.Type(value = AudioBotProductInitRequest.class, name = "AUDIO_BOT")
})
@Data
public abstract class AbstractProductInitRequest {

    private ProductType type;

    private String productName;

    private Long categoryId;

    private BigDecimal price;

    private boolean enabled;

    private ProductPeriod productPeriod;

}
