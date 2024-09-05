package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tags Controller", description = "Tags API")
public class TagController {
    private final TagService tagService;
    @GetMapping
    @Operation(summary = "Create tags")
    public List<TagWrapper> searchTags(@RequestParam("name") String name) {
        return tagService.searchTags(name);
    }
}
