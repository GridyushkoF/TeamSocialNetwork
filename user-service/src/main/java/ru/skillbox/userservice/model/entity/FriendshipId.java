package ru.skillbox.userservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipId implements Serializable {
    @Column(name = "account_id_from")
    private Long accountIdFrom;

    @Column(name = "account_id_to")
    private Long accountIdTo;
}