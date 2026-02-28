package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemNotificationRepository extends JpaSpecificationExecutor<SystemNotification>, JpaRepository<SystemNotification, Long> {
}
