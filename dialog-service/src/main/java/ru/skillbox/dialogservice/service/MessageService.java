package ru.skillbox.dialogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.commonlib.dto.statistics.CountDto;
import ru.skillbox.dialogservice.mapper.MessageMapper;
import ru.skillbox.dialogservice.model.dto.DialogRs;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.model.enums.MessageStatus;
import ru.skillbox.dialogservice.processor.DialogMessageProcessor;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageService {

    private static final String RECIPIENT_ID = "recipientId";
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final DialogMessageProcessor dialogMessageProcessor;
    private DialogService dialogService;

    private static Specification<Message> getAuthUserUnreadMessagesSpecification(Long authUserId) {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get(RECIPIENT_ID), authUserId),
                cb.equal(root.get("status"), MessageStatus.SENT)
        );
    }

    private static Specification<Message> getMessageSpecification(Long authUserId, Long participantId) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get("authorId"), authUserId),
                        cb.equal(root.get(RECIPIENT_ID), participantId)
                ),
                cb.and(
                        cb.equal(root.get("authorId"), participantId),
                        cb.equal(root.get(RECIPIENT_ID), authUserId)
                )
        );
    }

    @Autowired
    public void setDialogService(@Lazy DialogService dialogService) {
        this.dialogService = dialogService;
    }

    public CountDto getUnread(Long authUserId) {
        int messagesAmount = Integer.parseInt(String.valueOf(messageRepository
                .count(getAuthUserUnreadMessagesSpecification(authUserId))));
        log.info("Get unread messages amount - {}", messagesAmount);
        return new CountDto(messagesAmount);
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
                        .map(message -> {
                            message.setStatus(MessageStatus.READ);
                            return message;
                        })
                        .toList()
        );
    }
}
