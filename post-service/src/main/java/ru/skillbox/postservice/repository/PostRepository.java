package ru.skillbox.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.postservice.exception.PostNotFoundException;
import ru.skillbox.postservice.model.entity.Post;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findAll(Pageable pageable);
    default Post getPostByIdOrThrowException(Long id) {
        return this.findById(id).orElseThrow(() -> new PostNotFoundException("Can`t find post with id " + id));
    }
    int countByPublishDateBetween(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
}
