package ru.skillbox.authentication.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.entity.RefreshToken;
;

@Component
@Slf4j
public class RedisExpirationEvent {
    @EventListener
    public void handleRedisKyeExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken refreshToken = (RefreshToken) event.getValue();
        if (refreshToken == null) {
            throw new RuntimeException("Рефреш токен = нал в handleRedisKyeExpiredEvent");
        }
        log.info("sds");
    }
}
