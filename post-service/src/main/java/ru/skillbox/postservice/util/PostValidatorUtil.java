package ru.skillbox.postservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.postservice.exception.PostAccessException;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.repository.PostRepository;

@Component
@RequiredArgsConstructor
public class PostValidatorUtil {
    private final PostRepository postRepository;

    public boolean isPostDenied(Post post) {
        return !(post.isBlocked() || post.isDelete());
    }

    public boolean isPostDenied(Long postId) {
        return isPostDenied(postRepository.getPostByIdOrThrowException(postId));
    }

    public void throwExceptionIfPostNotValid(Long postId) {
        if (!isPostDenied(postId)) {
            throw new PostAccessException(postId);
        }
    }
    public void throwAccessExceptionIfUserNotAuthor(PostDto postToUpdate, Long authUserId) {
        Post post = postRepository.getPostByIdOrThrowException(postToUpdate.getId());
        if(!post.getAuthorId().equals(authUserId)) {
            throw new PostAccessException(postToUpdate.getId());
        }
    }
}
