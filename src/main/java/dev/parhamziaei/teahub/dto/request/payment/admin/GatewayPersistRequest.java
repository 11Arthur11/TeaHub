package dev.parhamziaei.teahub.dto.request.payment.admin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AqayePardakhtPersistRequest.class, name = "AQAYE_PARDAKHT")
})
@Data
public abstract class GatewayPersistRequest {

    @Schema(nullable = true)
    private String name;
    @Schema(nullable = true)
    private Boolean active;
    private PaymentGatewayType type;

}
