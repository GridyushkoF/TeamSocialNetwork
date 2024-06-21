package ru.skillbox.postservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skillbox.postservice.model.dto.PostDto;
import ru.skillbox.postservice.model.entity.Post;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostDto postToPostDto(Post post);
    Post postDtoToPost(PostDto postDto);
}
