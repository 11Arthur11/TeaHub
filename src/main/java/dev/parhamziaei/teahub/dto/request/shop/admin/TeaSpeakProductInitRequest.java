package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductInitRequest extends BaseProductInitRequest {

    private Integer maxClients;
    private Duration expiration;

}
