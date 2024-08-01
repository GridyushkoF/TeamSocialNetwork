package ru.skillbox.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.commonlib.dto.statistics.DateCountPointDto;
import ru.skillbox.postservice.exception.PostNotFoundException;
import ru.skillbox.postservice.model.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findAll(Pageable pageable);
    default Post getPostByIdOrThrowException(Long id) {
        return this.findById(id).orElseThrow(() -> new PostNotFoundException("Can`t find post with id " + id));
    }
    int countByPublishDateBetween(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
    @Query("SELECT new ru.skillbox.commonlib.dto.statistics.DateCountPointDto(" +
            "DATE_TRUNC('MONTH', e.publishDate), COUNT(e)) " +
            "FROM Post e " +
            "WHERE e.publishDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_TRUNC('MONTH', e.publishDate)")
    List<DateCountPointDto> countPostsPerMonth(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new ru.skillbox.commonlib.dto.statistics.DateCountPointDto(" +
            "DATE_TRUNC('HOUR', e.publishDate), COUNT(e)) " +
            "FROM Post e " +
            "WHERE e.publishDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_TRUNC('HOUR', e.publishDate)")
    List<DateCountPointDto> countPostsPerHour(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}

