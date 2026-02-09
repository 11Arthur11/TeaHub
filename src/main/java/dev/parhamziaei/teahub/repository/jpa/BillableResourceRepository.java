package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
