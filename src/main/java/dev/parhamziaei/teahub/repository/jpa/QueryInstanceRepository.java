package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;

import java.util.List;
import java.util.Optional;

public interface QueryInstanceRepository {

    Optional<QueryInstance> findById(Long id);
    Optional<QueryInstance> findByAddress(String ip, Integer port);
    List<QueryInstance> findAll();
    void save(QueryInstance queryInstance);
    void delete(QueryInstance queryInstance);
    void update(QueryInstance queryInstance);
    boolean existByAddress(String ip, Integer port);

}
