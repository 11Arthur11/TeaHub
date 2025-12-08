package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillableResourceRepository extends JpaRepository<BillableResource, Long> {
}
