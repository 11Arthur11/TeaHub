package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.repository.jpa.ResourceProvisioningStrategyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeaSpeakProvisionStrategyFactory {

    private final Map<ProvisionStrategy, TeaSpeakProvisionStrategyHandler> strategies;
    private final ResourceProvisioningStrategyRepository provisioningStrategyRepo;

    public TeaSpeakProvisionStrategyFactory(
            List<TeaSpeakProvisionStrategyHandler> handlers,
            ResourceProvisioningStrategyRepository provisioningStrategyRepo
    ) {
        this.strategies = handlers.stream()
                .collect(Collectors.toMap(TeaSpeakProvisionStrategyHandler::getType, h -> h));
        this.provisioningStrategyRepo = provisioningStrategyRepo;
    }

    public TeaSpeakProvisionStrategyHandler getStrategy() {
        return strategies.get(provisioningStrategyRepo.getTeaspeakStrategy());
    }

}
