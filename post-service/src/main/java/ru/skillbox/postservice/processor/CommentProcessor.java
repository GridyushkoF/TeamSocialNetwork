package ru.skillbox.postservice.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.notification.NotificationEvent;
import ru.skillbox.commonlib.notification.NotificationType;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.model.entity.CommentType;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.PostRepository;

@Component
@Log4j2
public class CommentProcessor {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Sinks.Many<NotificationEvent> sink;

    @Autowired
    public CommentProcessor(CommentRepository commentRepository, PostRepository postRepository, Sinks.Many<NotificationEvent> sink) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.sink = sink;
    }

    public void process(Comment comment) {
        Long userId;
        NotificationType notificationType;
        if (CommentType.POST.equals(comment.getCommentType())) {
            userId = postRepository.findById(comment.getPostId()).get().getAuthorId();
            notificationType = NotificationType.POST_COMMENT;
        } else {
            userId = commentRepository.findById(comment.getParentId()).get().getAuthorId();
            notificationType = NotificationType.COMMENT_COMMENT;
        }
        NotificationEvent event = NotificationEvent.builder()
                .id(comment.getId())
                .authorId(comment.getAuthorId())
                .userId(userId)
                .notificationType(notificationType)
                .content("'" + comment.getCommentText() + "'")
                .build();
        log.info("Message sent to notification service {}", event);
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
