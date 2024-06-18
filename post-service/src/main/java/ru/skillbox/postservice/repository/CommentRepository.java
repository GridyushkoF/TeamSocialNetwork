package ru.skillbox.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.postservice.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}