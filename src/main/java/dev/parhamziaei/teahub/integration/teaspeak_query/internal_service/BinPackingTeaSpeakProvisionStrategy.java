package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component("BIN_PACKING")
@RequiredArgsConstructor
public class BinPackingTeaSpeakProvisionStrategy implements TeaSpeakProvisionStrategyHandler {

    private final QueryInstanceRepository queryInstanceRepo;

    @Override
    public QueryInstance getProviderQueryInstance() {
        return queryInstanceRepo.findAll()
                .stream()
                .filter(queryInstance -> !queryInstance.isFull())
                .min(Comparator.comparing(QueryInstance::getId))
                .orElseThrow(QueryProvisionException::new);
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.BIN_PACKING;
    }
}
