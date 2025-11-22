package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;

import java.util.List;
import java.util.Optional;

public interface QueryInstanceRepository {

    Optional<QueryInstance> findById(Long id);
    Optional<QueryInstance> findByIp(String ip);
    List<QueryInstance> findAll();
    void save(QueryInstance queryInstance);
    void delete(QueryInstance queryInstance);
    void update(QueryInstance queryInstance);
    boolean existByIp(String ip);

}
