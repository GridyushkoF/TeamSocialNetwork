package ru.skillbox.userservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.commondto.dto.account.StatusCode;

@Entity
@Getter
@Setter
@IdClass(FriendshipId.class)
public class Friendship {

    @Id
    @Column(name = "account_id_from", columnDefinition = "BIGINT NOT NULL")
    private Long accountIdFrom;

    @Id
    @Column(name = "account_id_to", columnDefinition = "BIGINT NOT NULL")
    private Long accountIdTo;

    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

}

