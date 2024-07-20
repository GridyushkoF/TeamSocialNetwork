package ru.skillbox.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.userservice.model.entity.Friendship;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByAccountIdFromAndAccountIdTo(long accountIdFrom, long accountIdTo);

}