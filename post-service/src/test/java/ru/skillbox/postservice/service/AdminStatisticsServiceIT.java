package ru.skillbox.postservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.commonlib.dto.post.CommentType;
import ru.skillbox.commonlib.dto.post.LikeReactionType;
import ru.skillbox.commonlib.dto.post.PostType;
import ru.skillbox.commonlib.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.postservice.TestDependenciesContainer;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.service.admin.AdminStatisticsService;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class AdminStatisticsServiceIT extends TestDependenciesContainer {

    @Autowired
    private AdminStatisticsService adminStatisticsService;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        likeRepository.deleteAll();
        commentRepository.deleteAll();
        setupTestData();
    }

    private void setupTestData() {
        Post post1 = new Post();
        post1.setAuthorId(1L);
        post1.setBlocked(false);
        post1.setDelete(false);
        post1.setTime(ZonedDateTime.now().minusDays(15));
        post1.setTitle("Post 1");
        post1.setPostText("This is the first post.");
        post1.setType(PostType.POSTED);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setAuthorId(2L);
        post2.setBlocked(false);
        post2.setDelete(true);
        post2.setTime(ZonedDateTime.now().minusDays(1));
        post2.setTitle("Post 2");
        post2.setPostText("This is the second post.");
        post2.setType(PostType.POSTED);
        postRepository.save(post2);

        Comment comment1 = new Comment();
        comment1.setCommentType(CommentType.POST);
        comment1.setAuthorId(1L);
        comment1.setBlocked(false);
        comment1.setDelete(false);
        comment1.setTime(ZonedDateTime.now().minusDays(10));
        comment1.setPostId(post1.getId());
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setCommentType(CommentType.POST);
        comment2.setAuthorId(2L);
        comment2.setBlocked(true);
        comment2.setDelete(false);
        comment2.setTime(ZonedDateTime.now().minusDays(5));
        comment2.setPostId(post1.getId());
        commentRepository.save(comment2);


        Like like1 = new Like();
        like1.setUserId(1L);
        like1.setEntityType(LikeEntityType.COMMENT);
        like1.setEntityId(comment1.getId());
        like1.setReactionType(LikeReactionType.WOW);
        like1.setCreationDateTime(ZonedDateTime.now().minusDays(8));
        likeRepository.save(like1);

        Like like2 = new Like();
        like2.setUserId(2L);
        like2.setEntityType(LikeEntityType.POST);
        like2.setEntityId(post1.getId());
        like2.setReactionType(LikeReactionType.WOW);
        like2.setCreationDateTime(ZonedDateTime.now().minusDays(3));
        likeRepository.save(like2);
    }

    @Test
    void getCommentsStatistics_ReturnsCorrectStatistics() {
        PeriodRequestDto periodRequestDto = new PeriodRequestDto(ZonedDateTime.now().minusMonths(1), ZonedDateTime.now());
        AdminStatisticsDto statistics = adminStatisticsService.getCommentsStatistics(periodRequestDto);


        assertNotNull(statistics);
        assertEquals(2, statistics.getCount());
        assertEquals(1, statistics.getCountPerMonth().size());
        assertEquals(1, statistics.getCountPerHours().size());

    }

    @Test
    void getLikesStatistics_ReturnsCorrectStatistics() {
        PeriodRequestDto periodRequestDto = new PeriodRequestDto(ZonedDateTime.now().minusMonths(1), ZonedDateTime.now());
        AdminStatisticsDto statistics = adminStatisticsService.getLikesStatistics(periodRequestDto);


        assertNotNull(statistics);
        assertEquals(2, statistics.getCount());
        assertEquals(1, statistics.getCountPerMonth().size());
        assertEquals(1, statistics.getCountPerHours().size());

    }

    @Test
    void getPostsStatistics_ReturnsCorrectStatistics() {
        PeriodRequestDto periodRequestDto = new PeriodRequestDto(ZonedDateTime.now().minusMonths(1), ZonedDateTime.now());
        AdminStatisticsDto statistics = adminStatisticsService.getPostsStatistics(periodRequestDto);


        assertNotNull(statistics);
        assertEquals(2, statistics.getCount());
        assertEquals(1, statistics.getCountPerMonth().size());
        assertEquals(1, statistics.getCountPerHours().size());
    }
}