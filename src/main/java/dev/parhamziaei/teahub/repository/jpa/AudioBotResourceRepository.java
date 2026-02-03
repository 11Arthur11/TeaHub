package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AudioBotResourceRepository extends JpaSpecificationExecutor<AudioBotResource>, JpaRepository<AudioBotResource, Long> {

    @Query("SELECT a FROM TeaSpeakResource a WHERE a.id = :resourceId AND a.owner.id = :ownerId")
    Optional<AudioBotResource> findOneByOwnerId(@Param("ownerId") Long userId, @Param("resourceId")Long resourceId);

    default Optional<AudioBotResource> findByOneByPermission(User user, Long resourceId) {
        if (user.isStaff())
            return findById(resourceId);
        else
            return findOneByOwnerId(user.getId(), resourceId);
    }

}
