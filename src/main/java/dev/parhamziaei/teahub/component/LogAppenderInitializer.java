package dev.parhamziaei.teahub.component;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import dev.parhamziaei.teahub.service.LogWebSocketService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LogAppenderInitializer {


    private final LogWebSocketService service;

    @PostConstruct
    public void init(){


        WebSocketLogAppender.setService(service);



        LoggerContext context =
                (LoggerContext)
                        LoggerFactory.getILoggerFactory();



        WebSocketLogAppender appender =
                new WebSocketLogAppender();



        appender.setContext(context);
        appender.start();



        Logger root =
                context.getLogger(
                        Logger.ROOT_LOGGER_NAME
                );


        root.addAppender(appender);

    }

}