package ru.skillbox.userservice.model.entity;


import lombok.NoArgsConstructor;


import java.io.Serializable;


@NoArgsConstructor
public class FriendshipId implements Serializable {
    private Long accountIdFrom;

    private Long accountIdTo;

    public FriendshipId(Long accountIdFrom, Long accountIdTo) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
    }
}