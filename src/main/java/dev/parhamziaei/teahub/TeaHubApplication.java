package dev.parhamziaei.teahub;

import dev.parhamziaei.teahub.configuration.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CookieFactoryProperties.class,
        IPPanelProperties.class,
        JwtProperties.class,
        SessionProperties.class,
        InitializeProperties.class
})
public class TeaHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaHubApplication.class, args);
    }
}
