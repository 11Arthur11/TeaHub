package dev.parhamziaei.teahub.schedule.resource;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.kafka.producer.ResourceEventProducer;
import dev.parhamziaei.teahub.repository.jpa.ApplicationSettingRepository;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.BillableResourceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ResourceExpirationSchedule {

    private final BillableResourceRepository billableResourceRepo;
    private final ResourceEventProducer resourceEventProducer;
    private final ApplicationSettingRepository applicationSettingRepository;

    @Scheduled(cron = "0 */5 * * * *")
    public void resourceSuspendSchedule() {
        Specification<BillableResource> spec = BillableResourceSpecification.byStatus(ResourceStatus.ACTIVE);
        billableResourceRepo.findAll(spec).forEach(resource -> {
            if (resource.getExpiration().plus(Duration.ofMinutes(5)).isBefore(LocalDateTime.now()))
                resourceEventProducer.sendResourceExpiredEvent(resource.getId());
        });
    }

    @Scheduled(cron = "0 0 * * * *")
    public void resourceDeleteSchedule() {
        LocalDateTime resourceDeleteTime = LocalDateTime.now().minus(
                applicationSettingRepository.find()
                        .getResourceProperties()
                        .getResourceDeleteTimeAfterSuspend()
        );

        Specification<BillableResource> spec = BillableResourceSpecification.byStatus(ResourceStatus.PENDING_PROLONG);
        billableResourceRepo.findAll(spec).forEach(resource -> {
            if (resource.getExpiration().isBefore(resourceDeleteTime))
                resourceEventProducer.sendResourceDeleteEvent(resource.getId());
        });
    }

}
