package ru.skillbox.postservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.post.LikeDto;
import ru.skillbox.commonlib.dto.post.LikeReactionType;
import ru.skillbox.postservice.TestDependenciesContainer;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class LikeServiceIT extends TestDependenciesContainer {
    private Long postId;

    @BeforeEach
    void setUp() {
        postId = savePostInDbAndGet(generateTestPostDto(), 1L).getId();
        likeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        likeRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("Like a post")
    @Transactional
    void likePost() {
        LikeDto likeDto = new LikeDto("POST",LikeReactionType.HEART);
        likeService.likePost(postId,likeDto,1L);
        assertTrue(likeRepository.count() == 1);
    }

    @Test
    @DisplayName("Unlike a post")
    @Transactional
    void unlikePost() {
        LikeDto likeDto = new LikeDto();
        likeDto.setReactionType(LikeReactionType.HEART);
        likeService.likePost(postId, likeDto, 1L);
        likeService.unlikePost(postId, 1L);
        assertEquals(0, likeRepository.count());
    }

    @Test
    @DisplayName("Like a comment")
    @Transactional
    void likeComment() {
        Long commentId = createTestComment(postId).getId();

        LikeDto likeDto = new LikeDto();
        likeDto.setReactionType(null);
        likeService.likeComment(postId, commentId, 1L);
        assertEquals(1, likeRepository.count());
        Like savedLike = likeRepository.findByEntityTypeAndEntityIdAndUserId(LikeEntityType.COMMENT, commentId, 1L)
                .orElseThrow();
        assertEquals(1L, savedLike.getUserId());
    }

    @Test
    @DisplayName("Unlike a comment")
    @Transactional
    void unlikeComment() {
        Long commentId = createTestComment(postId).getId();

        LikeDto likeDto = new LikeDto();
        likeDto.setReactionType(null);
        likeService.likeComment(postId, commentId, 1L);
        likeService.unlikeComment(postId, commentId, 1L);
        assertEquals(0, likeRepository.count());
    }

    // Утилитарный метод для создания тестового комментария
    private Comment createTestComment(Long postId) {
        return saveCommentInDbAndGet(generateTestCommentDto(postId), 1L);
    }
}