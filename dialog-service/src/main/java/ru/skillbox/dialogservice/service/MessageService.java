package ru.skillbox.dialogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.dialogservice.mapper.MessageMapper;
import ru.skillbox.dialogservice.model.dto.*;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.model.enums.MessageStatus;
import ru.skillbox.dialogservice.processor.DialogMessageProcessor;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final DialogMessageProcessor dialogMessageProcessor;

    @Autowired
    @Lazy
    private DialogService dialogService;

    public DialogRs getUnread(Long authUserId) {
        List<Message> messages = messageRepository
                .findAll(getAuthUserUnreadMessagesSpecification(authUserId));
        log.info("Get unread messages - {}", messages);
        return new DialogRs(messageMapper.toDtoList(messages));
    }

    private static Specification<Message> getAuthUserUnreadMessagesSpecification(Long authUserId) {
        return (root, cq, cb) -> cb.and(
                cb.or(
                        cb.equal(root.get("authorId"), authUserId),
                        cb.equal(root.get("recipientId"), authUserId)
                ),
                cb.equal(root.get("status"), MessageStatus.SENT)
        );
    }

    public Page<MessageDto> getMessages(Long authUserId, Long conversationPartnerId,
                                        Integer page, String sort) {
        String[] sorts = sort.split(",");
        Pageable nextPage = PageRequest.of(page, Integer.MAX_VALUE,
                Sort.by(Sort.Direction.fromString(sorts[1]), sorts[0]));

        List<Message> messages = messageRepository.findAll(
                getMessageSpecification(authUserId, conversationPartnerId), nextPage).toList();

        log.info("Get messages {}", messages);

        List<MessageDto> pageList = messages.stream().map(messageMapper::toDto).toList();

        return new PageImpl<>(pageList, nextPage, messages.size());

    }

    private static Specification<Message> getMessageSpecification(Long authUserId, Long participantId) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get("authorId"), authUserId),
                        cb.equal(root.get("recipientId"), participantId)
                ),
                cb.and(
                        cb.equal(root.get("authorId"), participantId),
                        cb.equal(root.get("recipientId"), authUserId)
                )
        );
    }

    public void saveMessage(MessageDto messageDto) {
        messageDto.setId(null);
        messageDto.setStatus(MessageStatus.SENT);
        messageMapper.toDto(
                messageRepository.save(messageMapper.toEntity(messageDto))
        );
        dialogService.incrementUnreadMessagesCount(messageDto.getDialogId());
        log.info("Save message {}", messageDto);

        dialogMessageProcessor.process(messageDto.getAuthorId(), messageDto.getRecipientId(), messageDto.getMessageText());
    }

    public void updateDialogMessages(Long currentAuthUserId, Long dialogId) {
        log.info("Update dialog messages dialog id - {}", dialogId);
        messageRepository.saveAll(
                messageRepository.findByDialogId(dialogId)
                        .stream()
                        .filter(message -> message.getRecipientId().equals(currentAuthUserId))
                        .filter(message -> message.getStatus().equals(MessageStatus.SENT))
                        .peek(message -> message.setStatus(MessageStatus.READ))
                        .toList()
        );
    }
}
