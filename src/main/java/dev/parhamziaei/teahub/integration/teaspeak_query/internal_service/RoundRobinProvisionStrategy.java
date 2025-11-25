package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component("ROUND_ROBIN")
@RequiredArgsConstructor
public class RoundRobinProvisionStrategy implements TeaSpeakProvisionStrategyHandler {

    private final AtomicInteger pointer = new AtomicInteger(0);
    private final QueryInstanceRepository queryInstanceRepo;

    @Override
    public QueryInstance getProviderQueryInstance() {
        List<QueryInstance> available = queryInstanceRepo.findAll()
                .stream()
                .filter(qi -> !qi.isFull())
                .sorted(Comparator.comparing(QueryInstance::getId))
                .toList();

        if (available.isEmpty()) {
            throw new QueryProvisionException("No available query instances found");
        }

        int index = pointer.getAndIncrement();

        return available.get(Math.floorMod(index, available.size()));
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.ROUND_ROBIN;
    }
}