package ru.skillbox.postservice.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skillbox.commondto.dto.post.LikeReactionType;
import ru.skillbox.postservice.TestDependenciesContainer;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;
import ru.skillbox.postservice.model.entity.Post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeControllerIT extends TestDependenciesContainer {
    @BeforeEach
    public void doBeforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        clearAllRepositories();
        savePostInDbAndGet(generateTestPostDto(), 1L);
    }

    @Test
    void likePost_CorrectLiked() throws Exception {
        Post post = postRepository.findAll().get(0);
        Long userId = 1L;
        mockMvc.perform(post(apiPrefix + "/post/" + post.getId() + "/like")
                .header("id", userId)
        ).andExpect(status().isCreated());
        Like like = likeRepository.findAll().get(0);
        assertEquals(like.getEntityType(), LikeEntityType.POST);
        assertEquals(like.getEntityId(), post.getId());
        assertEquals(like.getUserId(), userId);
    }

    @Test
    void likePost_LikeAlreadyExists() throws Exception {
        Post post = postRepository.findAll().get(0);
        Long userId = 1L;
        saveTestLikeInDb(LikeEntityType.POST, post.getId(), userId);
        mockMvc.perform(post(apiPrefix + "/post/" + post.getId() + "/like")
                .header("id", userId)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void likePost_PostNotAccess() throws Exception {
        Long userId = 1L;
        mockMvc.perform(post(apiPrefix + "/post/" + 1145356356L + "/like")
                .header("id", userId)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void likePost_AuthNotCorrect() {
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post(apiPrefix + "/post/" + 11453563L + "/like"));
        });
    }

    @Test
    void unLikePost_CorrectUnlike() throws Exception {
        Post post = postRepository.findAll().get(0);
        Long userId = 1L;
        saveTestLikeInDb(LikeEntityType.POST, post.getId(), userId);
        mockMvc.perform(delete(apiPrefix + "/post/" + post.getId() + "/like")
                .header("id", userId)
        ).andExpect(status().isOk());
        assertTrue(likeRepository.findAll().isEmpty());
    }

    @Test
    void unLikePost_LikeNotExists() throws Exception {
        Post post = postRepository.findAll().get(0);
        Long userId = 1L;
        mockMvc.perform(delete(apiPrefix + "/post/" + post.getId() + "/like")
                .header("id", userId)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void likeComment_CorrectCreated() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        Comment comment = saveCommentInDbAndGet(generateTestCommentDto(postId), postId);
        mockMvc.perform(post(apiPrefix + "/post/" + postId
                + "/comment/" + comment.getId() + "/like")
                .header("id", 1L)
        ).andExpect(status().isCreated());
        assertEquals(1, likeRepository.findAll().size());
    }

    @Test
    void likeComment_CommentNotExists() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        mockMvc.perform(post(apiPrefix + "/post/" + postId
                + "/comment/" + 3547357L + "/like")
                .header("id", 1L)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void likeComment_PostNotExists() throws Exception {
        mockMvc.perform(post(apiPrefix + "/post/" + 26246256256L
                + "/comment/" + 1L + "/like")
                .header("id", 1L)
        ).andExpect(status().isBadRequest());
    }


    @Test
    void unLikeComment_Correct() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        Comment comment = saveCommentInDbAndGet(generateTestCommentDto(postId), postId);
        saveTestLikeInDb(LikeEntityType.COMMENT, comment.getId(), 1L);
        mockMvc.perform(delete(apiPrefix + "/post/" + postId
                        + "/comment/" + comment.getId() + "/like")
                        .header("id", 1L))
                .andExpect(status().isOk());
        assertTrue(likeRepository.findAll().isEmpty());
    }

    @Test
    void unLikeComment_CommentNotExists() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        mockMvc.perform(delete(apiPrefix + "/post/" + postId
                        + "/comment/" + 5356356356L + "/like")
                        .header("id", 1L))
                .andExpect(status().isBadRequest());
    }

    //-------------UTIL-METHODS--------------------
    public void saveTestLikeInDb(LikeEntityType likeEntityType, Long entityId, Long userId) {
        likeRepository.save(new Like(null, userId, likeEntityType, entityId, LikeReactionType.WOW));
    }
}
