package ru.skillbox.dialogservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.skillbox.dialogservice.TestDependenciesContainer;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.model.entity.Message;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class DialogServiceIT extends TestDependenciesContainer {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        dialogRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    @DisplayName("Get message by ID returns valid message")
    @Transactional
    void getMessageById_ReturnsValidMessage() {
        DialogDto dialogDto = generateTestDialogDto();
        Dialog dialog = saveDialogInDbAndGet(dialogDto);
        MessageDto messageDto = generateTestMessageDto(dialog.getId());
        Message message = saveMessageInDbAndGet(messageDto);
        assertEquals(messageDto.getDialogId(), message.getDialogId());
        assertEquals(messageDto.getAuthorId(), message.getAuthorId());
        assertEquals(messageDto.getRecipientId(), message.getRecipientId());
        assertEquals(messageDto.getStatus(), message.getStatus());
        assertEquals(messageDto.getMessageText(), message.getMessageText());
    }
}