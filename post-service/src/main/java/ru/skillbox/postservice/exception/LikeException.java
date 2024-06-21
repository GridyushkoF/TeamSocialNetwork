package ru.skillbox.postservice.exception;

public class LikeException extends RuntimeException {
    public LikeException(String message) {
        super(message);
    }
    public LikeException(Long postId, Long userId) {
        super("Like already exists like at post " + postId + " user id is " + userId);
    }
    public LikeException(Long postId, Long userId, Long commentId) {
        super("Like already exists at like at comment " + commentId + " user id is " + userId + " post id is " + postId);
    }

}
