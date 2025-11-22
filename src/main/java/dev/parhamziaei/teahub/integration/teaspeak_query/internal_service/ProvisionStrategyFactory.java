package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvisionStrategyFactory {

    private final Map<ProvisionStrategy, TeaSpeakProvisionStrategyHandler> strategies;

    public ProvisionStrategyFactory(List<TeaSpeakProvisionStrategyHandler> handlers) {
        this.strategies = handlers.stream()
                .collect(Collectors.toMap(TeaSpeakProvisionStrategyHandler::getType, h -> h));
    }

    public TeaSpeakProvisionStrategyHandler getStrategy(ProvisionStrategy type) {
        return strategies.get(type);
    }

}
