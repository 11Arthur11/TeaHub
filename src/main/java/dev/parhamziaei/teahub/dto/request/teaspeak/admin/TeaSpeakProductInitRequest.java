package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.BaseProductInitRequest;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductInitRequest extends BaseProductInitRequest {

    private Integer maxClients;
    private Duration expiration;
    private Money price;

}
