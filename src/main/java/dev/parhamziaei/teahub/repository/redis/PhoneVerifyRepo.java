package dev.parhamziaei.teahub.repository.redis;

import dev.parhamziaei.teahub.entity.redis.PhoneVerifySession;
import dev.parhamziaei.teahub.entity.redis.TwoFactorSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class PhoneVerifyRepo {

    private final RedisTemplate<String, PhoneVerifySession> redisTemplate;

    public String getKey(String sessionId) {
        return "register:2fa:" + sessionId;
    }

    public void save(String sessionId, PhoneVerifySession verifySession, Duration ttl) {
        redisTemplate.opsForValue().set(getKey(sessionId), verifySession, ttl);
    }

    public void save(String sessionId, PhoneVerifySession verifySession) {
        redisTemplate.opsForValue().set(getKey(sessionId), verifySession);
    }

    public PhoneVerifySession get(String sessionId) {
        return redisTemplate.opsForValue().get(getKey(sessionId));
    }

    public void remove(String sessionId) {
        redisTemplate.delete(getKey(sessionId));
    }

}
