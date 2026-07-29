package dev.parhamziaei.teahub.dto.response.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.valueobject.Money;
import dev.parhamziaei.teahub.valueobject.ProductPresentation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Duration;

@Data
@Schema(
        description = "Product detail response",
        oneOf = {
                TeaSpeakProductDetailAdminResponse.class,
                AudioBotProductDetailAdminResponse.class
        },
        discriminatorProperty = "productType"
)
public class AbstractProductDetailResponse {

    private Long id;

    private String categoryName;

    private String categorySlug;

    private String productName;

    private ProductPeriod period;

    private ProductType productType;

    private Long orderedResources;

    private Money price;

    private boolean enabled;

    private ProductPresentation presentation;

}
