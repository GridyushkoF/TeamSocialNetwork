package ru.skillbox.postservice.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.postservice.model.dto.PostDto;
import ru.skillbox.postservice.model.entity.Post;

@Mapper(componentModel = "spring")
@DecoratedWith(PostMapperDecorator.class)
public interface PostMapper {
    @Mapping(target = "tags", ignore = true)
    PostDto postToPostDto(Post post);
    @Mapping(target = "tags", ignore = true)
    Post postDtoToPost(PostDto postDto);
}
