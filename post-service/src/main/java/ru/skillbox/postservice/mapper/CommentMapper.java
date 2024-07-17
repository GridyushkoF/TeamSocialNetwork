package ru.skillbox.postservice.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.commondto.post.CommentDto;
import ru.skillbox.postservice.model.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto commentToCommentDto(Comment comment);

    Comment commentDtoToComment(CommentDto commentDto);
}
