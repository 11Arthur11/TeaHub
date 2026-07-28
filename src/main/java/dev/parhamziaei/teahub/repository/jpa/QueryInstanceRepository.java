package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.CountSummary;
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
    default List<QueryInstance> findProvisionCandidates() {
        return findAll().stream()
                .filter(queryInstance -> !queryInstance.isFull() && queryInstance.isActive())
                .toList();
    }
    default boolean isAnyProvisionCandidateAvailable() {
        return !findProvisionCandidates().isEmpty();
    }
    CountSummary countSummary();

}
