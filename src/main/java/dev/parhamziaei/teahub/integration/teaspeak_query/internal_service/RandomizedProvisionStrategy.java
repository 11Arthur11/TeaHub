package dev.parhamziaei.teahub.integration.teaspeak_query.internal_service;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component("RANDOMIZED")
@RequiredArgsConstructor
public class RandomizedProvisionStrategy implements TeaSpeakProvisionStrategyHandler {
    
    private final QueryInstanceRepository queryInstanceRepo;
    
    @Override
    public QueryInstance getProviderQueryInstance() {
        List<QueryInstance> available =  queryInstanceRepo.findAll()
                .stream()
                .filter(queryInstance -> !queryInstance.isFull())
                .toList();
        if (available.isEmpty())
            throw new QueryProvisionException();

        Random random = new Random();
        return available.get(random.nextInt(available.size()));
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.RANDOMIZED;
    }
}
