package ru.skillbox.postservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.post.CommentDto;
import ru.skillbox.commonlib.dto.post.CommentType;
import ru.skillbox.commonlib.dto.post.PostType;
import ru.skillbox.commonlib.dto.post.pages.PageCommentDto;
import ru.skillbox.postservice.TestDependenciesContainer;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.Post;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class CommentServiceIT extends TestDependenciesContainer {


    private Long postId;

    @BeforeEach
    void setUp() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setPostText("This is a test post.");
        post.setType(PostType.POSTED);
        postId = postRepository.save(post).getId();
        commentRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("Update comment")
    @Transactional
    void updateComment() {
        CommentDto commentDto = generateTestCommentDto(CommentType.POST);
        Comment savedComment = commentService.createNewComment(postId, commentDto, 1L);
        savedComment.setCommentText("Updated text");
        commentService.updateComment(postId, commentMapper.commentToCommentDto(savedComment), 1L);
        Comment updatedComment = commentRepository.findById(savedComment.getId()).orElseThrow();
        assertEquals("Updated text", updatedComment.getCommentText());
    }

    @Test
    @DisplayName("Delete comment")
    @Transactional
    void deleteComment() {
        CommentDto commentDto = generateTestCommentDto(CommentType.POST);
        Comment savedComment = commentService.createNewComment(postId, commentDto, 1L);
        commentService.deleteComment(postId, savedComment.getId(), 1L);
        assertTrue(commentRepository.findById(savedComment.getId()).get().isDelete());
    }

    @Test
    @DisplayName("Get comments on post")
    @Transactional
    void getCommentsOnPost() {
        CommentDto commentDto = generateTestCommentDto(CommentType.POST);
        commentService.createNewComment(postId, commentDto, 1L);
        PageCommentDto comments = commentService.getCommentsOnPost(postId, Pageable.unpaged(), 1L, false);
        assertNotNull(comments);
        assertFalse(comments.getContent().isEmpty());
    }

    @Test
    @DisplayName("Create new comment")
    @Transactional
    void createNewComment() {
        CommentDto commentDto = generateTestCommentDto(CommentType.POST);
        commentService.createNewComment(postId, commentDto, 1L);
        assertEquals(1, commentRepository.count());
    }

    @Test
    @DisplayName("Get sub-comments")
    @Transactional
    void getSubComments() {
        CommentDto parentCommentDto = generateTestCommentDto(CommentType.POST);
        Comment parentComment = commentService.createNewComment(postId, parentCommentDto, 1L);
        CommentDto subCommentDto = generateTestCommentDto(CommentType.COMMENT);
        subCommentDto.setParentId(parentComment.getId());
        commentService.createNewComment(postId, subCommentDto, 1L);
        PageCommentDto subComments = commentService.getSubComments(postId, parentComment.getId(), Pageable.unpaged(), 1L, false);
        assertNotNull(subComments);
        assertFalse(subComments.getContent().isEmpty());
        assertEquals(1, subComments.getContent().size());
    }

    private CommentDto generateTestCommentDto(CommentType type) {
        return CommentDto.builder()
                .commentText("Test comment")
                .authorId(1L)
                .postId(postId)
                .commentType(type)
                .build();
    }
}
