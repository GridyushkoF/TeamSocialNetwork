package ru.skillbox.authentication.model.entity.nosql;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@RedisHash("refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {
    @Id
    @Indexed
    private String id;
    @Indexed
    private Long userId;
    @Indexed
    private String token;
    @Indexed
    private Instant expiryDate;
}
