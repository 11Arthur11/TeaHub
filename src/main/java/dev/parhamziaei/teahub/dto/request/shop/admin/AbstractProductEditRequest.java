package dev.parhamziaei.teahub.dto.request.shop.admin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TeaSpeakProductEditRequest.class, name = "TEASPEAK")
})
@Data
public class AbstractProductEditRequest {

    private ProductType type;

    private String productName;

    private Long categoryId;

    private Money price;

}
