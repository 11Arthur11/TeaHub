package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.response.system.admin.LogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogWebSocketService {


    private final SimpMessagingTemplate template;


    public void send(LogEvent event){

        template.convertAndSend(
                "/topic/logs",
                event
        );

    }

}