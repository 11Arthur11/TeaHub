package dev.parhamziaei.teahub.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class OnlineUserRedisRepo {

    private final RedisTemplate<String, Long> redisTemplate;

    public String getKey(Long sessionId) {
        return "online:user:" + sessionId;
    }

    public void save(Long userId) {
        redisTemplate.opsForValue().set(getKey(userId), userId, Duration.ofMinutes(5));
    }

    public Boolean isOnline(Long userId) {
        return redisTemplate.hasKey(getKey(userId));
    }

    public Long countAll() {
        Set<String> keys =
                redisTemplate.keys("online:user:*");

        return keys == null ? 0L : (long) keys.size();
    }

    public void remove(Long userId) {
        redisTemplate.delete(getKey(userId));
    }

}
