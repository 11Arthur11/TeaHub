package dev.parhamziaei.teahub;

import dev.parhamziaei.teahub.configuration.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(exclude = {
        RedisRepositoriesAutoConfiguration.class
})
@EnableJpaRepositories(basePackages = "dev.parhamziaei.teahub.repository.jpa")
@EnableJdbcRepositories(basePackages = "dev.parhamziaei.teahub.repository.jdbc")
@EnableRedisRepositories(basePackages = "dev.parhamziaei.teahub.repository.redis")
@EnableConfigurationProperties({
        CookieFactoryProperties.class,
        IPPanelProperties.class,
        JwtProperties.class,
        SessionProperties.class,
        InitializeProperties.class,
        TicketServiceProperties.class,
        ImageStorageProperties.class,
        NotificationProperties.class,
        TelnetProperties.class,
        QueryInstanceProperties.class,
        PaymentServiceProperties.class,
        ApplicationSettingProperties.class
})
public class TeaHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaHubApplication.class, args);
    }
}
