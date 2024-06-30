package ru.skillbox.postservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.skillbox.postservice.mapper.CommentMapper;
import ru.skillbox.postservice.mapper.PostMapper;
import ru.skillbox.postservice.model.dto.CommentDto;
import ru.skillbox.postservice.model.dto.CommentType;
import ru.skillbox.postservice.model.dto.PostDto;
import ru.skillbox.postservice.model.dto.PostType;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.LikeRepository;
import ru.skillbox.postservice.repository.PostRepository;
import ru.skillbox.postservice.repository.TagRepository;
import ru.skillbox.postservice.service.PostService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class TestDependenciesContainer {
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected LikeRepository likeRepository;
    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected PostMapper postMapper;
    @Autowired
    protected CommentMapper commentMapper;

    protected MockMvc mockMvc;

    @Value("${app.apiPrefix}")
    protected String apiPrefix;
    protected Post savePostInDbAndGet(PostDto postDto, Long userId) {
        Post post = postMapper.postDtoToPost(postDto);
        post.setId(null);
        post.setAuthorId(userId);
        postRepository.save(post);
        return post;
    }
    protected Comment saveCommentInDbAndGet(CommentDto commentDto, Long postId) {
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setPostId(postId);
        comment.setId(null);
        commentRepository.save(comment);
        return comment;
    }
    protected PostDto generateTestPostDto() {
        return PostDto.builder()
                .title("Test Post")
                .postText("This is a test post")
                .authorId(1L)
                .type(PostType.POSTED)
                .tags(List.of("string"))
                .build();
    }
    protected CommentDto generateTestCommentDto(Long postId) {
        return CommentDto.builder()
                .commentText("Test comment text")
                .authorId(1L)
                .commentType(CommentType.POST)
                .postId(postId)
                .commentsCount(0L)
                .build();
    }
    protected void clearAllRepositories() {
        tagRepository.deleteAll();
        likeRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }
}