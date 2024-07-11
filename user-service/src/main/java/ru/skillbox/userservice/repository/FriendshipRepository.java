package ru.skillbox.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.userservice.model.dto.StatusCode;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    @Query("SELECT count(*) FROM Friendship f" +
            " WHERE f.id.accountIdFrom = :accountIdFrom AND f.statusCode = :statusCode")
    int countByAccountIdFromAndStatusCode(@Param("accountIdFrom") Long accountIdFrom,
                                          @Param("statusCode") StatusCode statusCode);

    List<Friendship> findByStatusCodeAndAccountIdFrom(StatusCode statusCode, Long accountIdFrom);
}