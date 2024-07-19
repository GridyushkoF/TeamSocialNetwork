package ru.skillbox.dialogservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dialogservice.model.dto.GetDialogsRs;
import ru.skillbox.dialogservice.model.dto.SetStatusMessageReadRs;
import ru.skillbox.dialogservice.model.dto.*;
import ru.skillbox.dialogservice.service.DialogService;
import ru.skillbox.dialogservice.service.MessageService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/dialogs")
public class DialogController {
    private final DialogService dialogService;
    private final MessageService messageService;

    @PutMapping("/{companionId}")
    public ResponseEntity<SetStatusMessageReadRs> setStatusRead(
            @PathVariable Long companionId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(dialogService.setStatusRead(companionId,currentAuthUserId));
    }
    @GetMapping
    public GetDialogsRs getDialogs(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage",defaultValue = "20") int itemPerPage,
            HttpServletRequest request
            ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return dialogService.getDialogs(offset, itemPerPage,currentAuthUserId, request);
    }
    @GetMapping("/unread")
    public ResponseEntity<UnreadCountRs> getUnreaded (
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(dialogService.getUnreaded(currentAuthUserId));
    }
    @GetMapping("/messages")
    public GetMessagesRs getMessages(
            @RequestParam Long recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "time,asc") String sort,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
//        return ResponseEntity.ok(service.getMessages(companionId, offset, itemPerPage, currentAuthUserId,request));
        return messageService.getMessages(currentAuthUserId, recipientId, page, size, sort);
    }

    @GetMapping("/recipientId/{id}")
    public DialogDto getDialog(HttpServletRequest request,
                               @PathVariable long id) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return dialogService.getDialog(currentAuthUserId, id);
    }

}
