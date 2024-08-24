package ru.skillbox.postservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.post.LikeDto;
import ru.skillbox.postservice.exception.LikeException;
import ru.skillbox.postservice.model.entity.Like;
import ru.skillbox.postservice.model.entity.LikeEntityType;
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
    public void likePost(Long postId, LikeDto likeDto, Long userId) {
        Optional<Like> likeOptional = getLikeIfPostValid(postId, userId);
        if (likeOptional.isEmpty()) {
            Like like = new Like(null, userId, LikeEntityType.POST, postId,likeDto.getReactionType(), null);
            likeRepository.save(like);
            log.info("Post with id " + postId + " was liked by user with id " + userId);
        } else {
            Like like = likeOptional.get();
            like.setReactionType(likeDto.getReactionType());
            likeRepository.save(like);
            log.info("Reaction on post with id " + postId + " was changed by user with id " + userId);
        }
    }
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Optional<Like> likeOptional = getLikeIfPostValid(postId, userId);
        if (likeOptional.isPresent()) {
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
            Like like = new Like(null,userId,LikeEntityType.COMMENT,commentId,null,null);
            likeRepository.save(like);
            log.info("Comment with id " + commentId + " was liked by user with id " + userId);
            return;
        }
        throw new LikeException(postId,commentId,userId);
    }
    @Transactional
    public void unlikeComment(Long postId, Long commentId, Long userId) {
        Optional<Like> likeOptional = getLikeOnCommentIfValid(postId,commentId, userId);
        if(likeOptional.isPresent()) {
            likeRepository.delete(likeOptional.get());
            log.info("Comment with id " + commentId + " was unliked by user with id " + userId);
            return;
        }
        throw new LikeException("Can`t unlike because like not exists, postId  "  + postId + " commentId " + commentId + " userId " + userId );
    }

}
