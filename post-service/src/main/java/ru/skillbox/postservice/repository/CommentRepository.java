package ru.skillbox.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.postservice.exception.CommentNotFoundException;
import ru.skillbox.postservice.model.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
    List<Comment> findAllByPostId(Long postId);
    int countByPostId(Long postId);
    Page<Comment> findAllByParentId(Long parentId, Pageable pageable);
    default Comment getByIdOrThrowException (Long id) {
        return findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }
}