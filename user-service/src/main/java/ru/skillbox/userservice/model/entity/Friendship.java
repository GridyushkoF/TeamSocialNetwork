package ru.skillbox.userservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.userservice.model.dto.StatusCode;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(FriendshipId.class)
public class Friendship {

    public Friendship(FriendshipId friendshipId) {
        this.id = friendshipId;
    }

    @EmbeddedId
    private FriendshipId id;

    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

//    @Id
//    @Column(name = "account_id_from", columnDefinition = "BIGINT NOT NULL")
//    private Long accountIdFrom;
//
//    @Id
//    @Column(name = "account_id_to", columnDefinition = "BIGINT NOT NULL")
//    private Long accountIdTo;
//
//    @Column(name = "status_code")
//    @Enumerated(EnumType.STRING)
//    private StatusCode statusCode;
//
//
}
