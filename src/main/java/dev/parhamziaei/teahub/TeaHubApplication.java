package dev.parhamziaei.teahub;

import dev.parhamziaei.teahub.configuration.properties.CookieFactoryProperties;
import dev.parhamziaei.teahub.configuration.properties.IPPanelProperties;
import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.configuration.properties.SessionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CookieFactoryProperties.class,
        IPPanelProperties.class,
        JwtProperties.class,
        SessionProperties.class,
})
public class TeaHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaHubApplication.class, args);
    }
}
