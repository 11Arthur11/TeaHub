package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeaSpeakResourceRepository extends JpaSpecificationExecutor<TeaSpeakResource>, JpaRepository<TeaSpeakResource, Long> {

    @Query("SELECT t FROM TeaSpeakResource t WHERE t.id = :resourceId AND t.owner.id = :ownerId")
    Optional<TeaSpeakResource> findOneByOwnerId(@Param("ownerId") Long userId, @Param("resourceId")Long resourceId);

    default Optional<TeaSpeakResource> findByOneByPermission(User user, Long resourceId) {
        if (user.isStaff())
            return findById(resourceId);
        else
            return findOneByOwnerId(user.getId(), resourceId);
    }

}
