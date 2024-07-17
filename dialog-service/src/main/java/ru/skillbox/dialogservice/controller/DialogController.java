package ru.skillbox.dialogservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dialogservice.model.dto.GetDialogsRs;
import ru.skillbox.dialogservice.model.dto.SetStatusMessageReadRs;
import ru.skillbox.dialogservice.model.dto.*;
import ru.skillbox.dialogservice.service.DialogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/dialogs")
public class DialogController {
    private final DialogService service;
    @PutMapping("/{companionId}")
    public ResponseEntity<SetStatusMessageReadRs> setStatusRead(
            @PathVariable Long companionId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.setStatusRead(companionId,currentAuthUserId));
    }
    @GetMapping
    public ResponseEntity<GetDialogsRs> getDialogs(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage",defaultValue = "20") int itemPerPage,
            HttpServletRequest request
            ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.getDialogs(offset, itemPerPage,currentAuthUserId,request));
    }
    @GetMapping("/unreaded")
    public ResponseEntity<UnreadCountRs> getUnreaded (
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.getUnreaded(currentAuthUserId));
    }
    @GetMapping("/messages")
    public ResponseEntity<GetMessagesRs> getMessages(
            @RequestParam(value = "companionId") Long companionId,
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage",defaultValue = "20") int itemPerPage,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(service.getMessages(companionId, offset, itemPerPage, currentAuthUserId,request));
    }

}
