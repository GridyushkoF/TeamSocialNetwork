package ru.skillbox.authentication.model.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("email_change_requests")
public class EmailChangeRequest {
    @Indexed
    private String id;
    @Indexed
    private String oldEmail;
    @Indexed
    private String newEmail;
    @Indexed
    private String currentTempCode;
}
