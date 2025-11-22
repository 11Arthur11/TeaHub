package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.springframework.stereotype.Component;

@Component("ROUND_ROBIN")
public class RoundRobinProvisionStrategyHandler implements TeaSpeakProvisionStrategyHandler {
    @Override
    public QueryInstance getProviderQueryInstance() {
        return null;
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.ROUND_ROBIN;
    }
}