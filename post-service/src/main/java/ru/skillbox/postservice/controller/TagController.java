package ru.skillbox.postservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.commonlib.dto.post.wrappers.TagWrapper;
import ru.skillbox.postservice.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/tag")
public class TagController {
    private final TagService tagService;
    @GetMapping
    public List<TagWrapper> searchTags(@RequestParam("name") String name) {
        return tagService.searchTags(name);
    }
}
