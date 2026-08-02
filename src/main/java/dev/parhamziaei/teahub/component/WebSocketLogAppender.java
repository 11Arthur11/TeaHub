package dev.parhamziaei.teahub.component;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import dev.parhamziaei.teahub.dto.response.system.admin.LogEvent;
import dev.parhamziaei.teahub.service.LogWebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Instant;
import java.util.Set;


public class WebSocketLogAppender
        extends AppenderBase<ILoggingEvent> {


    private static final Logger log = LoggerFactory.getLogger(WebSocketLogAppender.class);
    private static LogWebSocketService service;


    private final Set<String> allowedLoggers =
            Set.of(
                    "dev.parhamziaei.teahub"
            );



    public static void setService(
            LogWebSocketService service
    ){

        WebSocketLogAppender.service = service;

    }



    @Override
    protected void append(
            ILoggingEvent event
    ){

        if(service == null)
            return;


        boolean allowed =
                allowedLoggers.stream()
                        .anyMatch(
                                event.getLoggerName()::startsWith
                        );


        if(!allowed)
            return;



        LogEvent log =
                new LogEvent(

                        extractClassName(event.getLoggerName()),

                        event.getLevel()
                                .toString(),

                        event.getFormattedMessage(),

                        event.getThreadName(),

                        Instant.ofEpochMilli(
                                event.getTimeStamp()
                        )

                );



        service.send(log);

    }

    private String extractClassName(String logger) {
        String[] parts = logger.split("\\.");
        return parts[parts.length - 1];
    }

}