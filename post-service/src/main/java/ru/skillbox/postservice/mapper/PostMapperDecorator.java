package ru.skillbox.postservice.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.skillbox.postservice.model.dto.PostDto;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.model.entity.Tag;
import ru.skillbox.postservice.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostMapperDecorator implements PostMapper {

    @Autowired
    @Qualifier("delegate")
    private PostMapper delegate;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Post postDtoToPost(PostDto postDto) {
        Post post = delegate.postDtoToPost(postDto);
        List<Tag> dbTags = new ArrayList<>();
        postDto.getTags().forEach(tagName -> {
            Optional<Tag> tagOptional = tagRepository.findTagByName(tagName);
            dbTags.add(tagOptional.orElseGet(() -> {
                Tag tag = new Tag(tagName);
                tagRepository.save(tag);
                return tag;
            }));
        });
        if(post.getTags() == null) {
            post.setTags(new ArrayList<>());
        }
        post.getTags().addAll(dbTags);
        return post;
    }

    @Override
    public PostDto postToPostDto(Post post) {
        PostDto postDto = delegate.postToPostDto(post);
        if(postDto.getTags() == null) {
            postDto.setTags(new ArrayList<>());
        }
        postDto.getTags().addAll(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        return postDto;
    }
}