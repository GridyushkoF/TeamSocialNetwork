package ru.skillbox.postservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commondto.post.CommentType;
import ru.skillbox.postservice.mapper.CommentMapper;
import ru.skillbox.commondto.post.CommentDto;
import ru.skillbox.commondto.post.pages.PageCommentDto;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.LikeEntityType;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.LikeRepository;
import ru.skillbox.postservice.util.CommentValidatorUtil;
import ru.skillbox.postservice.util.PostValidatorUtil;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2

public class CommentService {
    private final CommentRepository commentRepository;
    private final PostValidatorUtil postValidator;
    private final CommentValidatorUtil commentValidator;
    private final LikeRepository likeRepository;
    private final CommentMapper commentMapper;

    private PageCommentDto buildPageCommentDto(Page<Comment> pageOfComments, Long userId) {
        List<CommentDto> commentDtoList = pageOfComments.getContent().stream()
                .map(commentMapper::commentToCommentDto)
                .peek(commentDto -> {
                    Long likesAmount = likeRepository.countAllByEntityTypeAndEntityId(LikeEntityType.COMMENT,commentDto.getId());
                    commentDto.setLikeAmount(likesAmount);
                    boolean myLike = likeRepository.existsByEntityTypeAndEntityIdAndUserId(
                            LikeEntityType.COMMENT,
                            commentDto.getId(),
                            userId
                            );
                    commentDto.setMyLike(myLike);
                })
                .toList();

        return PageCommentDto.builder()
                .totalElements(pageOfComments.getTotalElements())
                .totalPages(pageOfComments.getTotalPages())
                .number(pageOfComments.getNumber())
                .size(pageOfComments.getSize())
                .content(commentDtoList)
                .sort(pageOfComments.getSort())
                .first(pageOfComments.isFirst())
                .last(pageOfComments.isLast())
                .numberOfElements(pageOfComments.getNumberOfElements())
                .pageable(pageOfComments.getPageable())
                .empty(pageOfComments.isEmpty())
                .build();
    }

    @Transactional
    public void updateComment(Long postId, Long commentId, CommentDto resultCommentDto, Long authUserId) {
        resultCommentDto.setId(commentId);
        resultCommentDto.setPostId(postId);
        postValidator.throwExceptionIfPostNotValid(postId);
        commentValidator.throwExceptionIfCommentNotValidWithAuthor(commentId,authUserId);
        Comment comment = commentRepository.getByIdOrThrowException(commentId);
        if (comment.getPostId().equals(postId) && resultCommentDto.getId().equals(commentId)) {
            commentRepository.save(commentMapper.commentDtoToComment(resultCommentDto));
        }
        log.info("Comment with id " + commentId + " updated by commentDto: " + resultCommentDto);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long authUserId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        commentValidator.throwExceptionIfCommentNotValidWithAuthor(commentId,authUserId);
        Comment comment = commentRepository.getByIdOrThrowException(commentId);
        comment.setDelete(true);
        commentRepository.save(comment);
        likeRepository.deleteAll(likeRepository.findAllByEntityTypeAndEntityId(LikeEntityType.COMMENT,commentId));
        log.info("Comment with id " + commentId + " deleted!");
    }

    @Transactional
    public PageCommentDto getCommentsOnPost(Long postId, Pageable pageable, Long userId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        Page<Comment> commentsOnPost = commentRepository.findAllByPostId(postId, pageable);
        return buildPageCommentDto(commentsOnPost,userId);
    }

    @Transactional
    public void createNewComment(Long postId, CommentDto commentDto, Long authUserId) {
        if (Objects.isNull(commentDto.getCommentType())) {
            commentDto.setCommentType(CommentType.POST);
        } else if(Objects.nonNull(commentDto.getParentId())) {
            commentDto.setCommentType(CommentType.COMMENT);
        }
        postValidator.throwExceptionIfPostNotValid(postId);
        commentDto.setAuthorId(authUserId);
        commentDto.setId(null);
        commentDto.setPostId(postId);
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        commentRepository.save(comment);
        log.info("Comment created by dto:  " + commentDto);
    }

    @Transactional
    public PageCommentDto getSubComments(Long postId,
                                         Long commentId,
                                         Pageable page,
                                         Long userId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        Page<Comment> subCommentsPage = commentRepository.findAllByParentId(commentId, page);
        return buildPageCommentDto(subCommentsPage, userId);
    }
}
