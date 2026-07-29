package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.ResourceMetric;
import dev.parhamziaei.teahub.dto.response.dashboard.user.ResourceOverviewResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Optional;

public interface BillableResourceRepository extends JpaSpecificationExecutor<BillableResource>, JpaRepository<BillableResource, Long> {

    @Query("SELECT b FROM BillableResource b WHERE b.id = :resourceId AND b.owner.id = :ownerId")
    Optional<BillableResource> findOneByOwnerId(@Param("ownerId") Long userId, @Param("resourceId")Long resourceId);

    default Optional<BillableResource> findOneByPermission(User user, Long resourceId) {
        if (user.isStaff())
            return findById(resourceId);
        else
            return findOneByOwnerId(user.getId(), resourceId);
    }

    @Query("""
        SELECT new dev.parhamziaei.teahub.dto.response.dashboard.user.ResourceOverviewResponse(
            COUNT(r),
            SUM(CASE WHEN r.resourceStatus = 'ACTIVE' THEN 1 ELSE 0 END),
            SUM(CASE WHEN r.resourceStatus = 'PENDING_PROLONG' THEN 1 ELSE 0 END)
        )
        FROM BillableResource r
        WHERE r.owner.id = :userId
    """)
    ResourceOverviewResponse getOverview(@Param("userId") Long userId);

    @Query("""
        SELECT new dev.parhamziaei.teahub.dto.response.dashboard.admin.ResourceMetric(
            COUNT(r),
            SUM(CASE WHEN r.resourceStatus = 'ACTIVE' THEN 1 ELSE 0 END),
            SUM(CASE WHEN r.resourceStatus = 'PENDING_PROLONG' THEN 1 ELSE 0 END),
            SUM(CASE WHEN r.resourceStatus = 'DEPLOYING' THEN 1 ELSE 0 END)
        )
        FROM BillableResource r
    """)
    ResourceMetric getResourceMetric();

    @Query("""
        SELECT COUNT(r) FROM BillableResource r WHERE r.product.id = :productId
    """)
    Long countByProductId(@Param("productId") Long productId);

}
