package ru.skillbox.postservice.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.commonlib.dto.post.wrappers.TagWrapper;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.model.entity.Tag;
import ru.skillbox.postservice.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PostMapperDecorator implements PostMapper {
    private final PostMapper delegate;
    private final TagRepository tagRepository;

    @Autowired
    public PostMapperDecorator(@Qualifier("delegate") PostMapper delegate, TagRepository tagRepository) {
        this.delegate = delegate;
        this.tagRepository = tagRepository;
    }

    @Override
    public Post postDtoToPost(PostDto postDto) {
        Post post = delegate.postDtoToPost(postDto);
        return convertTagsAndGet(postDto, post);
    }

    public Post convertTagsAndGet(PostDto postDto, Post post) {
        List<Tag> dbTags = new ArrayList<>();
        if (postDto.getTags() == null) {
            postDto.setTags(new ArrayList<>());
        }
        postDto.getTags().forEach(tagWrapper -> {
            Optional<Tag> tagOptional = tagRepository.findTagByName(tagWrapper.getName());
            dbTags.add(tagOptional.orElseGet(() -> {
                Tag tag = new Tag(tagWrapper.getName());
                tagRepository.save(tag);
                return tag;
            }));
        });
        if (post.getTags() == null) {
            post.setTags(new ArrayList<>());
        }
        post.getTags().addAll(dbTags);
        return post;
    }

    @Override
    public PostDto postToPostDto(Post post) {
        PostDto postDto = delegate.postToPostDto(post);
        if (postDto.getTags() == null) {
            postDto.setTags(new ArrayList<>());
        }
        if (post.getTags() == null) {
            post.setTags(new ArrayList<>());
        }
        postDto.getTags()
                .addAll(post.getTags()
                        .stream().map(Tag::getName)
                        .map(TagWrapper::new)
                        .collect(Collectors.toSet()));
        return postDto;
    }
}