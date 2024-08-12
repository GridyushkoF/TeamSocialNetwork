package ru.skillbox.postservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.skillbox.commonlib.dto.post.wrappers.TagWrapper;
import ru.skillbox.postservice.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TagService {
    private final TagRepository tagRepository;
    public List<TagWrapper> searchTags(String name) {
        return tagRepository.findTagWrappersByNameContaining(name);
    }
}
