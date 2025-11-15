package dev.parhamziaei.teahub.repository.redis;

import dev.parhamziaei.teahub.entity.redis.ForgotPasswordSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ForgotPasswordRepo {

    private final RedisTemplate<String, ForgotPasswordSession> redisTemplate;

    public String getKey(String sessionId) {
        return "forgot-password:code-session:" + sessionId;
    }

    public void save(String sessionId, ForgotPasswordSession session, Duration ttl) {
        redisTemplate.opsForValue().set(getKey(sessionId), session, ttl);
    }

    public void save(String sessionId, ForgotPasswordSession session) {
        redisTemplate.opsForValue().set(getKey(sessionId), session);
    }

    public ForgotPasswordSession get(String sessionId) {
        return redisTemplate.opsForValue().get(getKey(sessionId));
    }

    public void remove(String sessionId) {
        redisTemplate.delete(getKey(sessionId));
    }

}
