package ru.skillbox.postservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skillbox.postservice.TestDependenciesContainer;
import ru.skillbox.commonlib.dto.post.CommentDto;
import ru.skillbox.commonlib.dto.post.pages.PageCommentDto;;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CommentControllerIT extends TestDependenciesContainer {

    @BeforeEach
    void initBeforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        clearAllRepositories();
        savePostInDbAndGet(generateTestPostDto(), 1L);
        Long postId = postRepository.findAll().get(0).getId();
        saveCommentInDbAndGet(generateTestCommentDto(postId), postId);
    }

    @Test
    @DisplayName("creating a post, comment on that and update it, result expected as correct test")
    void updateComment_CorrectUpdate() throws Exception {
        getUpdateCommentResultActions(1L).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("creating a post, comment on that and update it, result expected as not access test")
    void updateComment_NotAccess() throws Exception {
        getUpdateCommentResultActions(2L).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("creating a post, comment that and delete, result expected as OK")
    void deleteComment_CorrectDelete() throws Exception {
        getDeleteCommentResultActions(1L).andExpect(status().isOk());
    }

    @Test
    @DisplayName("creating a post, comment that and delete, result expected as not access")
    void deleteComment_NotAccess() throws Exception {
        getDeleteCommentResultActions(2L).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("getting comments on the post and checking correctness of response test")
    void getCommentsOnPost_CorrectResponse() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        int page = 0;
        int size = 10;
        List<String> sort = List.of("id,asc");

        MvcResult result = mockMvc.perform(get(apiPrefix + "/post/" + postId + "/comment")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", String.join(",", sort))
                        .param("isDeleted", "false")
                        .header("id","1")
                )
                .andExpect(status().isOk())
                .andReturn();

        PageCommentDto pageCommentDto = objectMapper.readValue(result.getResponse().getContentAsString(), PageCommentDto.class);

        assertFalse(pageCommentDto.getContent().isEmpty());
        pageCommentDto.getContent().forEach(comment -> {
            assertNotNull(comment.getId());
            assertNotNull(comment.getCommentText());
            assertNotNull(comment.getAuthorId());
        });
        assertEquals(10, pageCommentDto.getSize());
        assertEquals(0, pageCommentDto.getNumber());
        assertTrue(pageCommentDto.isFirst());
        assertTrue(pageCommentDto.isLast());
        assertEquals(1, pageCommentDto.getNumberOfElements());
        assertEquals(Sort.by(Sort.Direction.ASC, "id"), pageCommentDto.getSort());
    }

    @Test
    @DisplayName("get sub comments returns OK status")
    void getSubComments_StatusIsOk() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        Long commentId = commentRepository.findAll().get(0).getId();
        int page = 0;
        int size = 10;
        List<String> sort = List.of("id,asc");

        mockMvc.perform(get(apiPrefix + "/post/" + postId + "/comment/" + commentId + "/subcomment")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", String.join(",", sort))
                        .param("isDeleted", "false")
                        .header("id","1")
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    //-----------------------------UTIL-METHODS----------------------
    private ResultActions getUpdateCommentResultActions(long userId) throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        CommentDto commentDto = commentMapper.commentToCommentDto(commentRepository.findAll().get(0));
        String commentJson = objectMapper.writeValueAsString(commentDto);
        return mockMvc.perform(put(apiPrefix + "/post/"
                + postId + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commentJson)
                .header("id", userId));
    }

    private ResultActions getDeleteCommentResultActions(Long userId) throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        Long commentId = commentRepository.findAll().get(0).getId();
        return mockMvc.perform(delete(apiPrefix + "/post/"
                + postId + "/comment/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("id", userId));
    }
}