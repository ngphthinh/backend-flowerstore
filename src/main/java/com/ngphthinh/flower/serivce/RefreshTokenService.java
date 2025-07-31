package com.ngphthinh.flower.serivce;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private static final String REFRESH_PREFIX = "refresh:";

    @Value("${jwt.refreshable-duration}")
    private Long REFRESHABLE_DURATION;

    private final RedisTemplate<String, String> redis;

    public RefreshTokenService(@Qualifier("redisTemplate") RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    public void saveRefreshToken(String refreshToken, String userId) {
        redis.opsForValue().set(getKey(refreshToken), userId, REFRESHABLE_DURATION, TimeUnit.DAYS);
    }


    public boolean isRefreshTokenExists(String refreshToken) {
        return Boolean.TRUE.equals(redis.hasKey(getKey(refreshToken)));
    }

    public void deleteRefreshToken(String refreshToken) {
        redis.delete(getKey(refreshToken));
    }

    private String getKey(String refreshToken) {
        return REFRESH_PREFIX + refreshToken;
    }

    public String getUserId(String refreshToken) {
        return redis.opsForValue().get(getKey(refreshToken));
    }

}
