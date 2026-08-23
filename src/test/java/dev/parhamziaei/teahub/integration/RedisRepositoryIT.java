package dev.parhamziaei.teahub.integration;

import dev.parhamziaei.teahub.configuration.RedisConfiguration;
import dev.parhamziaei.teahub.configuration.properties.RedisConfigurationProperties;
import dev.parhamziaei.teahub.entity.redis.TwoFactorSession;
import dev.parhamziaei.teahub.repository.redis.OnlineUserRedisRepo;
import dev.parhamziaei.teahub.repository.redis.TwoFactorRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
class RedisRepositoryIT {

    @Container
    static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    private LettuceConnectionFactory connectionFactory;
    private OnlineUserRedisRepo onlineUsers;
    private TwoFactorRepo twoFactors;

    @BeforeEach
    void setUp() {
        RedisConfiguration configuration = new RedisConfiguration();
        connectionFactory = configuration.lettuceConnectionFactory(new RedisConfigurationProperties(
                REDIS.getHost(),
                REDIS.getMappedPort(6379),
                ""
        ));
        connectionFactory.afterPropertiesSet();
        connectionFactory.start();

        RedisTemplate<String, Long> onlineTemplate = configuration.onlineUserRedisTemplate(connectionFactory);
        onlineTemplate.afterPropertiesSet();
        onlineUsers = new OnlineUserRedisRepo(onlineTemplate);

        RedisTemplate<String, TwoFactorSession> twoFactorTemplate = configuration.twoFactorRedisTemplate(connectionFactory);
        twoFactorTemplate.afterPropertiesSet();
        twoFactors = new TwoFactorRepo(twoFactorTemplate);
    }

    @AfterEach
    void tearDown() {
        connectionFactory.getConnection().serverCommands().flushAll();
        connectionFactory.destroy();
    }

    @Test
    void onlineUserKeysCanBeCountedAndRemoved() {
        onlineUsers.save(10L);
        onlineUsers.save(20L);

        assertTrue(onlineUsers.isOnline(10L));
        assertEquals(2L, onlineUsers.countAll());

        onlineUsers.remove(10L);
        assertFalse(onlineUsers.isOnline(10L));
        assertEquals(1L, onlineUsers.countAll());
    }

    @Test
    void twoFactorSessionRoundTripsAsJsonWithTtl() {
        TwoFactorSession session = new TwoFactorSession("9000000000", "hashed-code");

        twoFactors.save("session-1", session, Duration.ofMinutes(1));

        TwoFactorSession restored = twoFactors.get("session-1");
        assertNotNull(restored);
        assertEquals(session.getPhoneNumber(), restored.getPhoneNumber());
        assertEquals(session.getCode(), restored.getCode());

        twoFactors.remove("session-1");
        assertNull(twoFactors.get("session-1"));
    }
}
