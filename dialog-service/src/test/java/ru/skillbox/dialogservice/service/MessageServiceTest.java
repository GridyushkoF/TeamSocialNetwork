package ru.skillbox.dialogservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.skillbox.commonlib.dto.statistics.CountDto;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.model.enums.MessageStatus;
import ru.skillbox.dialogservice.repository.DialogRepository;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class MessageServiceTest {

    @Autowired
    protected MessageService messageService;
    @Autowired
    protected DialogRepository dialogRepository;
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected DialogService dialogService;

    @BeforeEach
    void setUp() {

        dialogService.getDialog(1L, 2L);
        Message message = Message.builder()
                .status(MessageStatus.SENT)
                .dialogId(1L)
                .authorId(1L)
                .recipientId(2L)
                .messageText("test message")
                .time(Instant.now())
                .build();
        messageRepository.save(message);
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @Test
    @DisplayName("test get unread message from db, return message")
    void testGetUnread() {
        CountDto unreadMessagesAmount = messageService.getUnread(2L);
        assertNotNull(unreadMessagesAmount);
        assertEquals(1, unreadMessagesAmount.getCount());
    }

    @Test
    @DisplayName("test get messages from db, return message with message text")
    void testGetMessages() {
        String sort = "time,asc";
        Page<MessageDto> messageDtoPage = messageService.getMessages(1L, 2L, 0, sort);
        assertNotNull(messageDtoPage);
        assertEquals(1, messageDtoPage.getTotalElements());
        assertEquals("test message", messageDtoPage.getContent().get(0).getMessageText());
    }

    @Test
    @DisplayName("test save message to db, return message with message text")
    void testSaveMessage() {
        List<Message> messageList = messageRepository.findByDialogId(1L);
        assertNotNull(messageList);
        assertEquals(1, messageList.size());

        MessageDto messageDto = MessageDto.builder()
                .status(MessageStatus.SENT)
                .dialogId(1L)
                .authorId(2L)
                .recipientId(1L)
                .messageText("test response")
                .time(Instant.now())
                .build();
        messageService.saveMessage(messageDto);

        messageList = messageRepository.findByDialogId(1L);
        assertNotNull(messageList);
        assertEquals(2, messageList.size());
    }

    @Test
    @DisplayName("test update messages of dialog to db, return message with message text")
    void testUpdateDialogMessages() {
        Message message = Message.builder()
                .status(MessageStatus.SENT)
                .dialogId(1L)
                .authorId(2L)
                .recipientId(1L)
                .messageText("test response")
                .time(Instant.now())
                .build();
        messageRepository.save(message);

        CountDto unreadMessagesAmount = messageService.getUnread(1L);
        assertEquals(1, unreadMessagesAmount.getCount());

        messageService.updateDialogMessages(1L, 1L);

        CountDto newUnreadMessagesAmount = messageService.getUnread(1L);
        assertEquals(0, newUnreadMessagesAmount.getCount());
    }
}