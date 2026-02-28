package dev.parhamziaei.teahub.schedule.resource;

import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import dev.parhamziaei.teahub.repository.jpa.SystemNotificationRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.SystemNotificationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationExpirationSchedule {

    private final SystemNotificationRepository systemNotificationRepo;

    @Scheduled(cron = "0 */2 * * * *")
    public void expiration() {
        Specification<SystemNotification> spec = SystemNotificationSpecification.byExpired();
        systemNotificationRepo.delete(spec);
    }

}
