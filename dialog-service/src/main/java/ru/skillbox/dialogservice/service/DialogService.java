package ru.skillbox.dialogservice.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dialogservice.exception.DialogNotFoundException;
import ru.skillbox.dialogservice.exception.NotFoundException;
import ru.skillbox.dialogservice.mapper.DialogMapperDecorator;
import ru.skillbox.dialogservice.mapper.MessageMapper;
import ru.skillbox.dialogservice.model.dto.*;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.repository.DialogRepository;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                companionId,currentAuthUserId, MessageStatus.SENT);
        unreadMessages.forEach(message -> {
            message.setStatus(MessageStatus.READ);
            messageRepository.save(message);
        });
        Optional<Dialog> dialogOptional = dialogRepository.findByMembersWithoutOrder(currentAuthUserId,companionId);
        if(dialogOptional.isEmpty()) {
            throw new DialogNotFoundException(currentAuthUserId,companionId);
        }
        Dialog dialog = dialogOptional.get();
        dialog.setUnreadCount(0L);
        dialogRepository.save(dialog);

        return new SetStatusMessageReadRs(
                null,
                System.currentTimeMillis(),
                new SetStatusMessageReadDto("successful read!"),
                null
        );
    }

    public GetDialogsRs getDialogs(int offset,
                                   int itemPerPage,
                                   Long currentAuthUserId,
                                   HttpServletRequest request) {
        Pageable pageable = generatePageableByOffsetAndPerPage(offset, itemPerPage);
        Page<Dialog> dialogPage = dialogRepository.findAll(
                getAuthUserDialogsSpecification(currentAuthUserId),
                pageable);
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
                        e.printStackTrace();
                        throw new RuntimeException();
                    }

                }).toList();
    }

    public UnreadCountRs getUnreaded(
            Long currentAuthUserId) {
        Long totalUnreadCount = dialogRepository.findAll(
                getAuthUserDialogsSpecification(currentAuthUserId),
                Pageable.unpaged())
                .stream()
                .mapToLong(Dialog::getUnreadCount)
                .sum();
        return new UnreadCountRs("amount of unread messages in all dialogs",
                null,
                System.currentTimeMillis(),
                new UnreadCountDto(totalUnreadCount),
                null);
    }

//    public GetMessagesRs getMessages(Long companionId,
//                                     int offset,
//                                     int itemPerPage,
//                                     Long currentAuthUserId,
//                                     HttpServletRequest request) {
//        Pageable pageable = generatePageableByOffsetAndPerPage(offset, itemPerPage);
//        Page<Message> messagePage = messageRepository.findAllByMembers(currentAuthUserId,companionId,pageable);
//        List<MessageDto> messageDtos = messagePage.getContent()
//                .stream()
//                .map(messageMapper::dialogToDialogDto)
//                .toList();
//        List<MessageShortDto> messageShortDtos = messageDtos.stream().map(messageDto -> new MessageShortDto(messageDto.getId(),
//                messageDto.getAuthorId(),
//                messageDto.getTime(),
//                messageDto.getMessageText())).toList();
//
//        return new GetMessagesRs(
//                null,
//                null,
//                System.currentTimeMillis(),
//                (int) messagePage.getTotalElements(),
//                offset,
//                itemPerPage,
//                messageShortDtos
//        );

//    }

    private static Pageable generatePageableByOffsetAndPerPage(int offset, int itemPerPage) {
        int pageNumber = offset / itemPerPage;
        return PageRequest.of(pageNumber, itemPerPage);
    }


    private static Specification<Dialog> getDialogSpecification(Long authUserId, Long participantId) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get("member1Id"), authUserId),
                        cb.equal(root.get("member2Id"), participantId)
                ),
                cb.and(
                        cb.equal(root.get("member1Id"), participantId),
                        cb.equal(root.get("member2Id"), authUserId)
                )
        );
    }
    private static Specification<Dialog> getAuthUserDialogsSpecification(Long authUserId) {
        return (root, cq, cb) ->
                cb.or(
                        cb.equal(root.get("member1Id"), authUserId),
                        cb.equal(root.get("member2Id"), authUserId)
                );
    }

    private Dialog getDialogById(Long id) {
        return dialogRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                MessageFormat.format("Диалог с id {0} не найден", id)
                        )
                );
    }

    public void incrementUnreadMessagesCount(Long id) {
        Dialog dialog = getDialogById(id);
        dialog.setUnreadCount(dialog.getUnreadCount() + 1);
        dialogRepository.save(dialog);
    }

    public DialogDto getDialog(Long authUserId, Long conversationPartnerId) {
        DialogDto dialogDto = dialogMapperDecorator.dialogToDialogDto(
                dialogRepository.findAll(getDialogSpecification(authUserId, conversationPartnerId))
                        .stream()
                        .findFirst()
                        .orElseGet(() -> {
                            Dialog newDialog = new Dialog();
                            newDialog.setMember1Id(authUserId);
                            newDialog.setMember2Id(conversationPartnerId);
                            newDialog.setUnreadCount(0L);
                            return dialogRepository.save(newDialog);
                        })
        );
        log.info("Get dialog dialog id {}", dialogDto);
        return dialogDto;
    }
}
