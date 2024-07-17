package ru.skillbox.userservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.commondto.account.StatusCode;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Friendship {
    public Friendship(FriendshipId friendshipId) {
        this.id = friendshipId;
    }

    @EmbeddedId
    private FriendshipId id;

    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

}
