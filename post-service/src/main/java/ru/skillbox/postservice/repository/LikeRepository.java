package ru.skillbox.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.commonlib.dto.post.ReactionDto;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByEntityTypeAndEntityIdAndUserId(LikeEntityType likeEntityType, Long entityId, Long userId);
    List<Like> findAllByEntityTypeAndEntityId(LikeEntityType likeEntityType, Long entityId);
    Long countAllByEntityTypeAndEntityId(LikeEntityType likeEntityType, Long entityId);
    boolean existsByEntityTypeAndEntityIdAndUserId(LikeEntityType likeEntityType, Long entityId,Long userId);
    @Query("SELECT new ru.skillbox.commonlib.dto.post.ReactionDto(l.reactionType, COUNT(l)) " +
            "FROM Like l " +
            "WHERE l.entityType = :entityType AND l.entityId = :entityId " +
            "GROUP BY l.reactionType")
    List<ReactionDto> findReactionsGroupedByType(@Param("entityType") LikeEntityType entityType,
                                                 @Param("entityId") Long entityId);
}

