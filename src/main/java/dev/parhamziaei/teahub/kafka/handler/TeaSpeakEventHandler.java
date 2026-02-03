package dev.parhamziaei.teahub.kafka.handler;

import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import dev.parhamziaei.teahub.service.TeaSpeakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeaSpeakEventHandler {

    private final TeaSpeakService teaSpeakService;

    public void handeTeaSpeakDeployEvent(TeaSpeakDeployEvent event) {
        try {
            teaSpeakService.deployInstance(event.getBaseResourceId(), event.getMaxClients());
        } catch (Exception e) {
            log.error("Exception on TeaSpeakEventHandler -> {}", e.getMessage(), e);
        }
    }

}
