package dev.parhamziaei.teahub.configuration;

import dev.parhamziaei.teahub.configuration.properties.RedisConfigurationProperties;
import dev.parhamziaei.teahub.entity.redis.PhoneVerifySession;
import dev.parhamziaei.teahub.entity.redis.TwoFactorSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean // TODO: This bean can be removed, spring will do this automatically
    public LettuceConnectionFactory lettuceConnectionFactory(RedisConfigurationProperties cfg) {
        RedisStandaloneConfiguration redisCfg = new RedisStandaloneConfiguration();
        redisCfg.setHostName(cfg.host());
        redisCfg.setPort(cfg.port());
        redisCfg.setPassword(cfg.password());
        return new LettuceConnectionFactory(redisCfg);
    }

    @Bean
    public RedisTemplate<String, TwoFactorSession> twoFactorRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, TwoFactorSession> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(TwoFactorSession.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, PhoneVerifySession> forgotPasswordRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, PhoneVerifySession> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(PhoneVerifySession.class));
        return redisTemplate;
    }

}
