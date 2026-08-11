package dev.parhamziaei.teahub.schedule.resource;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.kafka.producer.ResourceEventProducer;
import dev.parhamziaei.teahub.repository.jpa.ApplicationSettingRepository;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.BillableResourceSpecification;
import dev.parhamziaei.teahub.valueobject.ProductPeriodSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceExpirationSchedule {

    private final BillableResourceRepository billableResourceRepo;
    private final ResourceEventProducer resourceEventProducer;
    private final ApplicationSettingRepository applicationSettingRepository;

    @Scheduled(cron = "0 */1 * * * *")
    public void resourceSuspendSchedule() {
        log.debug("Starting ResourceSuspendSchedule...");
        Specification<BillableResource> spec = BillableResourceSpecification.byStatus(ResourceStatus.ACTIVE);
        billableResourceRepo.findAll(spec).forEach(resource -> {
            if (resource.getExpiration().plus(Duration.ofMinutes(5)).isBefore(LocalDateTime.now()))
                resourceEventProducer.sendResourceExpiredEvent(resource.getId());
        });
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void resourceDeleteSchedule() {
        log.debug("Starting ResourceDeleteSchedule...");
        ProductPeriodSettings settings = applicationSettingRepository.find().getProductPeriodSettings();

        Specification<BillableResource> spec = BillableResourceSpecification.byStatus(ResourceStatus.PENDING_PROLONG);
        billableResourceRepo.findAll(spec).forEach(resource -> {
            switch (resource.getProduct().getPeriod()) {
                case HOURLY -> {
                    if (resource.getExpiration().plus(settings.getHourly().getSuspendDeleteAfter()).isBefore(LocalDateTime.now()))
                        resourceEventProducer.sendResourceDeleteEvent(resource.getId());
                }
                case DAILY -> {
                    if (resource.getExpiration().plus(settings.getDaily().getSuspendDeleteAfter()).isBefore(LocalDateTime.now()))
                        resourceEventProducer.sendResourceDeleteEvent(resource.getId());
                }
                default -> {
                    if (resource.getExpiration().plus(settings.getMonthly().getSuspendDeleteAfter()).isBefore(LocalDateTime.now()))
                        resourceEventProducer.sendResourceDeleteEvent(resource.getId());
                }
            }
        });
    }

}
