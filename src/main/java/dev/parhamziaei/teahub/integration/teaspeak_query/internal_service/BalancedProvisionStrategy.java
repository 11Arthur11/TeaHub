package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("BALANCED")
@RequiredArgsConstructor
public class BalancedProvisionStrategy implements TeaSpeakProvisionStrategyHandler {

    private final QueryInstanceRepository queryInstanceRepository;

    @Override
    @Transactional
    public QueryInstance getProviderQueryInstance() {
        return queryInstanceRepository.findAll()
                .stream()
                .filter(queryInstance -> !queryInstance.isFull())
                .min(Comparator.comparing(qi -> qi.getInstances().size()))
                .orElseThrow(QueryProvisionException::new);
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.BALANCED;
    }

}
