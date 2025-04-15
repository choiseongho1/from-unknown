package com.fromunknown.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;


    public boolean isKeyExists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setKeyWithExpire(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }
}
