package ru.skillbox.postservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.commonlib.dto.post.PostSearchDto;
import ru.skillbox.commonlib.dto.post.pages.PagePostDto;
import ru.skillbox.postservice.TestDependenciesContainer;
import ru.skillbox.postservice.model.entity.Post;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class PostServiceIT extends TestDependenciesContainer {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("Get post by ID returns valid post")
    @Transactional
    void getPostById_ReturnsValidPost() {
        PostDto postDto = generateTestPostDto();
        Post savedPost = postService.createNewPost(postDto, postDto.getAuthorId());
        PostDto result = postService.getPostById(savedPost.getId());
        assertEquals(savedPost.getId(), result.getId());
        assertEquals(postDto.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("Update post correctly")
    @Transactional
    void updatePost_CorrectUpdate() {
        PostDto postDto = generateTestPostDto();
        Post savedPost = postService.createNewPost(postDto, postDto.getAuthorId());
        PostDto updatedPostDto = new PostDto();
        updatedPostDto.setId(savedPost.getId());
        updatedPostDto.setPostText("Updated text");
        postService.updatePost(updatedPostDto, savedPost.getAuthorId());
        Post updatedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        assertEquals("Updated text", updatedPost.getPostText());
    }

    @Test
    @DisplayName("Delete post correctly")
    @Transactional
    void deletePost_CorrectDelete() {
        PostDto postDto = generateTestPostDto();
        Post savedPost = postService.createNewPost(postDto, postDto.getAuthorId());
        postService.deletePostById(savedPost.getId(), savedPost.getAuthorId());
        assertTrue(postRepository.findById(savedPost.getId()).get().isDelete());
    }

    @Test
    @DisplayName("Search posts with specific criteria")
    @Transactional
    void searchPosts() {
        PostDto postDto = generateTestPostDto();
        postService.createNewPost(postDto, postDto.getAuthorId());
        PostSearchDto searchDto = new PostSearchDto();
        PagePostDto result = postService.searchPosts(searchDto, Pageable.unpaged(), 1L);
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Search specific post by filters")
    @Transactional
    void searchSpecificPostByFilters() {
        PostDto postDto = generateTestPostDto();
        Post savedPost = postService.createNewPost(postDto, postDto.getAuthorId());
        PostSearchDto searchDto = new PostSearchDto();
        searchDto.setTitle(postDto.getTitle());
        searchDto.setDelete(false);
        PagePostDto result = postService.searchPosts(searchDto, Pageable.unpaged(), postDto.getAuthorId());
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(savedPost.getId(), result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Search post with non-matching filters")
    @Transactional
    void searchPostWithNonMatchingFilters() {
        PostDto postDto = generateTestPostDto();
        postService.createNewPost(postDto, postDto.getAuthorId());
        PostSearchDto searchDto = new PostSearchDto();
        searchDto.setTitle("Non-existing title");
        PagePostDto result = postService.searchPosts(searchDto, Pageable.unpaged(), postDto.getAuthorId());
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Create new post")
    @Transactional
    void createNewPost() {
        PostDto postDto = generateTestPostDto();

        postService.createNewPost(postDto, postDto.getAuthorId());

        assertEquals(1, postRepository.count());
    }

    //-----------------------------UTIL--------------------------
    @Override
    public PostDto generateTestPostDto() {
        return PostDto.builder()
                .title("Test Post")
                .postText("This is a test post.")
                .authorId(1L)
                .build();
    }
}
