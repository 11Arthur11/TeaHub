package dev.parhamziaei.teahub.kafka.handler;

import dev.parhamziaei.teahub.kafka.event.resource.ResourceDeleteEvent;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceExpiredEvent;
import dev.parhamziaei.teahub.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceEventHandler {

    private final ResourceService resourceService;

    public void handleExpiredEvent(ResourceExpiredEvent event) {
        resourceService.resourceExpiredHandler(event.getResourceId());
    }

    public void handleResourceDeleteEvent(ResourceDeleteEvent event) {
        resourceService.deleteResource(event.getResourceId());
    }

}
