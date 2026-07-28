package dev.parhamziaei.teahub.dto.request.resource.admin;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProvisioningStrategyRequest {

    private ProvisionStrategy provisionStrategy;

}
