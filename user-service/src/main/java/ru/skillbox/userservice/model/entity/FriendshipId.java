package ru.skillbox.userservice.model.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class FriendshipId implements Serializable {
    private Long accountIdFrom;

    private Long accountIdTo;

    public FriendshipId(Long accountIdFrom, Long accountIdTo) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
    }
}