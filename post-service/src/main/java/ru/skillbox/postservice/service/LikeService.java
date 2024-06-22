package ru.skillbox.postservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.postservice.exception.LikeException;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.LikeRepository;
import ru.skillbox.postservice.repository.PostRepository;
import ru.skillbox.postservice.util.CommentValidatorUtil;
import ru.skillbox.postservice.util.PostValidatorUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikeService {
    private final PostValidatorUtil postValidator;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentValidatorUtil commentValidator;
    private final CommentRepository commentRepository;

    private Optional<Like> getLikeIfPostValid(Long postId, Long userId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        return likeRepository.findByEntityTypeAndEntityIdAndUserId(LikeEntityType.POST,postId,userId);
    }
    private Optional<Like> getLikeOnCommentIfValid(Long postId, Long commentId, Long userId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        commentValidator.throwExceptionIfCommentNotValid(commentId);
        return likeRepository.findByEntityTypeAndEntityIdAndUserId(LikeEntityType.COMMENT,commentId,userId);
    }
    @Transactional
    public void likePost(Long postId, Long userId) {
        Optional<Like> likeOptional = getLikeIfPostValid(postId, userId);
        if (likeOptional.isEmpty()) {
            Post post = postRepository.getPostByIdOrThrowException(postId);
            Like like = new Like(null, userId, LikeEntityType.POST, postId);
            likeRepository.save(like);
            post.getLikes().add(like);
            postRepository.save(post);
            log.info("Post with id " + postId + " was liked by user with id " + userId);
            return;
        }
        throw new LikeException(postId, userId);
    }
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Optional<Like> likeOptional = getLikeIfPostValid(postId, userId);
        if (likeOptional.isPresent()) {
            Post post = postRepository.getPostByIdOrThrowException(postId);
            post.getLikes().remove(likeOptional.get());
            postRepository.save(post);
            likeRepository.delete(likeOptional.get());
            log.info("Post with id " + postId + " was unliked by user with id " + userId);
            return;
        }
        throw new LikeException("Can`t unlike because like not exists, postId  "  + postId + " userId " + userId);
    }
    @Transactional
    public void likeComment(Long postId, Long commentId, Long userId) {
        Optional<Like> likeOptional = getLikeOnCommentIfValid(postId,commentId, userId);
        if(likeOptional.isEmpty()) {
            Comment comment = commentRepository.getByIdOrThrowException(commentId);
            Like like = new Like(null,userId,LikeEntityType.COMMENT,commentId);
            likeRepository.save(like);
            comment.getLikes().add(like);
            commentRepository.save(comment);

            log.info("Comment with id " + commentId + " was liked by user with id " + userId);
            return;
        }
        throw new LikeException(postId,commentId,userId);
    }
    @Transactional
    public void unlikeComment(Long postId, Long commentId, Long userId) {
        Optional<Like> likeOptional = getLikeOnCommentIfValid(postId,commentId, userId);
        if(likeOptional.isPresent()) {
            Comment comment = commentRepository.getByIdOrThrowException(commentId);
            comment.getLikes().remove(likeOptional.get());
            commentRepository.save(comment);
            likeRepository.delete(likeOptional.get());
            log.info("Comment with id " + commentId + " was unliked by user with id " + userId);
            return;
        }
        throw new LikeException("Can`t unlike because like not exists, postId  "  + postId + " commentId " + commentId + " userId " + userId );
    }
}