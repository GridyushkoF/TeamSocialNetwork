package ru.skillbox.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

}