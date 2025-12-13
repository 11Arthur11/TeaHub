package dev.parhamziaei.teahub.dto.request.shop.admin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.parhamziaei.teahub.enums.ProductPeriod;
import dev.parhamziaei.teahub.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TeaSpeakProductRequest.class, name = "TEASPEAK_PRODUCT")
})

@Data
public abstract class AbstractProductRequest {

    private ProductType type;

    private String productName;

    private Long categoryId;

    private BigDecimal price;

    private boolean enabled;

    private ProductPeriod productPeriod;
//    @Schema(type = "string", description = "Duration in format like 1h, 2d only support days & hour", example = "30d")
//    @JsonDeserialize(using = ExpirationDurationDeserializer.class)
//    private Duration expiration;

}
