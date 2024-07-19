package ru.skillbox.dialogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.dialogservice.mapper.MessageMapper;
import ru.skillbox.dialogservice.model.dto.DialogRs;
import ru.skillbox.dialogservice.model.dto.GetMessagesRs;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.dto.MessageStatus;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    @Autowired
    @Lazy
    private DialogService dialogService;

    public DialogRs getUnread(Principal principal) {
        Long principalId = (long) principal.getName().hashCode();
        List<Message> messages = messageRepository
                .findAll(getAuthUserUnreadMessagesSpecification(principalId));
        log.info("Get unread messages - {}", messages);
        return new DialogRs(messageMapper.toDtoList(messages));
    }

    private static Specification<Message> getAuthUserUnreadMessagesSpecification(Long principalId) {
        return (root, cq, cb) -> cb.and(
                cb.or(
                        cb.equal(root.get("senderId"), principalId),
                        cb.equal(root.get("recipientId"), principalId)
                ),
                cb.equal(root.get("status"), MessageStatus.SENT)
        );
    }

    public GetMessagesRs getMessages(Long authUserId, Long conversationPartnerId,
                                     Integer page, Integer size, String sort) {
        String[] sorts = sort.split(",");
        Page<Message> messages = messageRepository.findAll(
                getMessageSpecification(authUserId, conversationPartnerId),
                PageRequest.of(
                        page, size, Sort.by(Sort.Direction.fromString(sorts[1]), sorts[0])
                )
        );

        log.info("Get messages {}", messages);

        return new GetMessagesRs(
                null,
                null,
                Instant.now().getEpochSecond(),
                (int) messages.getTotalElements(),
                page * size,
                size,
                messageMapper.toDtoList(messages.getContent())
        );
    }

    private static Specification<Message> getMessageSpecification(Long principalId, Long participantId) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get("authorId"), principalId),
                        cb.equal(root.get("recipientId"), participantId)
                ),
                cb.and(
                        cb.equal(root.get("authorId"), participantId),
                        cb.equal(root.get("recipientId"), principalId)
                )
        );
    }

    public void saveMessage(MessageDto messageDto) {
        messageDto.setId(null);
        messageDto.setStatus(MessageStatus.SENT);
        messageMapper.dialogToDialogDto(
                messageRepository.save(messageMapper.toEntity(messageDto))
        );
        dialogService.incrementUnreadMessagesCount(messageDto.getDialogId());
        log.info("Save message {}", messageDto);
    }

    public void updateDialogMessages(Long principalId, Long dialogId) {
        log.info("Update dialog messages dialog id - {}", dialogId);
        messageRepository.saveAll(
                messageRepository.findByDialogId(dialogId)
                        .stream()
                        .filter(message -> message.getRecipientId().equals(principalId))
                        .filter(message -> message.getStatus().equals(MessageStatus.SENT))
                        .peek(message -> {
                            message.setStatus(MessageStatus.READ);
                        })
                        .toList()
        );
    }
}
