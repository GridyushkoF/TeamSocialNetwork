package ru.skillbox.authentication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RecoveryPasswordRequest {
    private long temp;
    private String email;

    public RecoveryPasswordRequest() {
        this.temp = System.currentTimeMillis();
    }

    public RecoveryPasswordRequest(long temp, String email) {
        this.temp = temp;
        this.email = email;
    }
}
