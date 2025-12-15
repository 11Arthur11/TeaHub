package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.teaspeak.QueryInstanceStatus;

import java.util.List;
import java.util.Optional;

public interface QueryInstanceRepository {

    Optional<QueryInstance> findById(Long id);
    Optional<QueryInstance> findByAddress(String ip, Integer port);
    List<QueryInstance> findAll();
    List<QueryInstance> findByStatus(QueryInstanceStatus status);
    void save(QueryInstance queryInstance);
    void delete(QueryInstance queryInstance);
    void update(QueryInstance queryInstance);
    boolean existByAddress(String ip, Integer port);

}
