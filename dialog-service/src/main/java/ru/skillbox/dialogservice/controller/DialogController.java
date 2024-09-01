package ru.skillbox.dialogservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.statistics.CountDto;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.service.DialogService;
import ru.skillbox.dialogservice.service.MessageService;


@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/dialogs")
@SecurityRequirement(name = "bearerAuth")
public class DialogController {
    private final DialogService dialogService;
    private final MessageService messageService;

    @PutMapping("/{id}")
    public DialogDto updateDialog(HttpServletRequest request,
                                  @PathVariable Long id) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return dialogService.updateDialog(currentAuthUserId, id);
    }

    @GetMapping
    public Page<DialogDto> getDialogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "unreadCount,desc") String sort,
            HttpServletRequest request
            ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return dialogService.getDialogs(page, sort, currentAuthUserId);
    }
    @GetMapping("/unread")
    public CountDto getUnread (
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return messageService.getUnread(currentAuthUserId);
    }

    @GetMapping("/messages")
    public Page<MessageDto> getMessages(
            @RequestParam Long recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "time,asc") String sort,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return messageService.getMessages(currentAuthUserId, recipientId, page, sort);
    }

    @GetMapping("/recipientId/{id}")
    public DialogDto getDialog(HttpServletRequest request,
                               @PathVariable long id) {
        Long authUserId = Long.parseLong(request.getHeader("id"));
        return dialogService.getDialog(authUserId, id);
    }

}
