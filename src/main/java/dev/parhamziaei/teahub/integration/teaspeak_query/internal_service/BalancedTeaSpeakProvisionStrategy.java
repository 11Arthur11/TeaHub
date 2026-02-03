package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component("BALANCED")
@RequiredArgsConstructor
public class BalancedTeaSpeakProvisionStrategy implements TeaSpeakProvisionStrategyHandler {

    private final QueryInstanceRepository queryInstanceRepository;

    @Override
    @Transactional
    public QueryInstance getProviderQueryInstance() {
        return queryInstanceRepository.findProvisionCandidates()
                .stream()
                .min(Comparator.comparing(qi -> qi.getInstances().size()))
                .orElseThrow(() ->  new QueryProvisionException("No query instances found with " + getType() + " strategy"));
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.BALANCED;
    }

}
