package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.springframework.stereotype.Component;

@Component("BIN_PACKING")
public class BinPackingProvisionStrategy implements TeaSpeakProvisionStrategyHandler {
    @Override
    public QueryInstance getProviderQueryInstance() {
        return null;
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.BIN_PACKING;
    }
}
