package ru.skillbox.userservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class FriendshipId implements Serializable {

    @Column(name = "account_id_from")
    private Long accountIdFrom;

    @Column(name = "account_id_to")
    private Long accountIdTo;

    public FriendshipId(Long accountIdFrom, Long accountIdTo) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(accountIdFrom, that.accountIdFrom) && Objects.equals(accountIdTo, that.accountIdTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountIdFrom, accountIdTo);
    }
}
