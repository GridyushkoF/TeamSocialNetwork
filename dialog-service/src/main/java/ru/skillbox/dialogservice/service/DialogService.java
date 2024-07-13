package ru.skillbox.dialogservice.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dialogservice.exception.DialogNotFoundException;
import ru.skillbox.dialogservice.mapper.DialogMapperDecorator;
import ru.skillbox.dialogservice.mapper.MessageMapper;
import ru.skillbox.dialogservice.model.dto.*;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.repository.DialogRepository;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class DialogService {
    private final MessageRepository messageRepository;
    private final DialogRepository dialogRepository;
    private final MessageMapper messageMapper;
    private final DialogMapperDecorator dialogMapperDecorator;
    @Transactional
    public SetStatusMessageReadRs setStatusRead(Long companionId,
                                                Long currentAuthUserId) {
        List<Message> unreadMessages =  messageRepository.findAllByAuthorIdAndRecipientIdAndStatus(
                companionId,currentAuthUserId, MessageStatus.SEND);
        unreadMessages.forEach(message -> {
            message.setStatus(MessageStatus.READ);
            messageRepository.save(message);
        });
        Dialog dialog = getDialogOrCreateIfNotExists(companionId, currentAuthUserId);
        dialog.setUnreadCount(0L);
        dialogRepository.save(dialog);

        return new SetStatusMessageReadRs(
                null,
                System.currentTimeMillis(),
                new SetStatusMessageReadDto("successful read!"),
                null
        );
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Dialog getDialogOrCreateIfNotExists(Long companionId, Long currentAuthUserId) {
        Optional<Dialog> dialogOptional = dialogRepository.findByMembersWithoutOrder(currentAuthUserId, companionId);
        Dialog dialog = null;
        if(dialogOptional.isEmpty()) {
            dialog = new Dialog(null,0L, currentAuthUserId, companionId);
            dialogRepository.save(dialog);
        } else {
            dialog = dialogOptional.get();
        }
        return dialog;
    }

    public GetDialogsRs getDialogs(int offset,
                                   int itemPerPage,
                                   Long currentAuthUserId,
                                   HttpServletRequest request) {
        Pageable pageable = generatePageableByOffsetAndPerPage(offset, itemPerPage);
        Page<Dialog> dialogPage = dialogRepository.findAllByMember1IdOrMember2Id(currentAuthUserId,pageable);
        List<DialogDto> dialogDtos = mapDialogsToDialogsDtos(currentAuthUserId, request, dialogPage);
        return new GetDialogsRs(
                null,
                null,
                System.currentTimeMillis(),
                (int) dialogPage.getTotalElements(),
                offset,
                itemPerPage,
                currentAuthUserId,
                dialogDtos
        );
    }

    private List<DialogDto> mapDialogsToDialogsDtos(Long currentAuthUserId, HttpServletRequest request, Page<Dialog> dialogPage) {
        return dialogPage.getContent()
                .stream()
                .map(dialog -> {
                    try {
                        Long companionId = dialog.getMember1Id().equals(currentAuthUserId) ? dialog.getMember2Id() : dialog.getMember1Id();
                        return dialogMapperDecorator.dialogToDialogDto(dialog,companionId, request);
                    }catch (Exception e) {
                        throw new RuntimeException();
                    }

                }).toList();
    }

    public UnreadCountRs getUnread(
            Long currentAuthUserId) {
        Long totalUnreadCount = dialogRepository.findAllByMember1IdOrMember2Id(currentAuthUserId, Pageable.unpaged())
                .stream()
                .mapToLong(Dialog::getUnreadCount)
                .sum();
        return new UnreadCountRs("amount of unread messages in all dialogs",
                null,
                System.currentTimeMillis(),
                new UnreadCountDto(totalUnreadCount),
                null);
    }

    public GetMessagesRs getMessages(Long companionId,
                                     Long currentAuthUserId,
                                     Pageable pageable) {

        Page<Message> messagePage = messageRepository.findAllByMembers(currentAuthUserId,companionId,pageable);
        List<MessageDto> messageDtos = messagePage.getContent()
                .stream()
                .map(messageMapper::dialogToDialogDto)
                .toList();
        List<MessageShortDto> messageShortDtos = messageDtos.stream().map(messageDto -> new MessageShortDto(messageDto.getId(),
                messageDto.getAuthorId(),
                messageDto.getTime(),
                messageDto.getMessageText())).toList();

        return new GetMessagesRs(
                null,
                null,
                System.currentTimeMillis(),
                (int) messagePage.getTotalElements(),
                (int) pageable.getOffset(),
                pageable.getPageSize(),
                messageShortDtos
        );

    }

    private static Pageable generatePageableByOffsetAndPerPage(int offset, int itemPerPage) {
        int pageNumber = offset / itemPerPage;
        return PageRequest.of(pageNumber, itemPerPage);
    }
}
