package ru.skillbox.dialogservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commondto.ContentableDto;
import ru.skillbox.dialogservice.model.dto.GetDialogsRs;
import ru.skillbox.dialogservice.model.dto.SetStatusMessageReadRs;
import ru.skillbox.dialogservice.model.dto.*;
import ru.skillbox.dialogservice.service.DialogService;
import ru.skillbox.dialogservice.util.SortCreatorUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/dialogs")
public class DialogController {
    private final DialogService service;
    @GetMapping("recipientId/{recipientId}")
    public ResponseEntity<SetStatusMessageReadRs> setStatusRead(
            @PathVariable Long recipientId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.setStatusRead(recipientId,currentAuthUserId));
    }
    @GetMapping
    public ResponseEntity<ContentableDto> getDialogs(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage",defaultValue = "20") int itemPerPage,
            HttpServletRequest request
            ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(new ContentableDto(List.of(service.getDialogs(offset, itemPerPage,currentAuthUserId,request))));
    }
    @GetMapping("/unread")
    public ResponseEntity<UnreadCountRs> getUnread(
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.getUnread(currentAuthUserId));
    }
    @GetMapping("/messages")
    public ResponseEntity<GetMessagesRs> getMessages(
            @RequestParam(value = "recipientId") Long recipientId,
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam("sort") List<String> sort,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.getMessages(recipientId,currentAuthUserId,
                PageRequest.of(page,size, SortCreatorUtil.createSort(sort))));
    }
}
