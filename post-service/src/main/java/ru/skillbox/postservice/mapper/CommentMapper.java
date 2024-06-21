package ru.skillbox.postservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skillbox.postservice.model.dto.CommentDto;
import ru.skillbox.postservice.model.entity.Comment;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto commentToCommentDto(Comment comment);

    Comment commentDtoToComment(CommentDto commentDto);
}
