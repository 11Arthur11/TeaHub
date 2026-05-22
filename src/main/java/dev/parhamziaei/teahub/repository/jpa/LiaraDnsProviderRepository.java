package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.LiaraDnsProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiaraDnsProviderRepository extends JpaRepository<LiaraDnsProvider, Long> {

    default Optional<LiaraDnsProvider> find() {
        return findById(LiaraDnsProvider.STATIC_ID);
    }

}
