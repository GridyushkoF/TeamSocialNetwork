package ru.skillbox.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;

import java.util.List;
import java.util.Optional;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByEntityTypeAndEntityIdAndUserId(LikeEntityType likeEntityType, Long entityId, Long userId);
    List<Like> findAllByEntityTypeAndEntityId(LikeEntityType likeEntityType, Long entityId);
}
