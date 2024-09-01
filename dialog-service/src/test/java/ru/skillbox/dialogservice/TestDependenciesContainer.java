package ru.skillbox.dialogservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.skillbox.dialogservice.mapper.DialogMapper;
import ru.skillbox.dialogservice.mapper.MessageMapper;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.repository.DialogRepository;
import ru.skillbox.dialogservice.repository.MessageRepository;
import ru.skillbox.dialogservice.service.DialogService;
import ru.skillbox.dialogservice.service.MessageService;

import java.time.Instant;

@SpringBootTest
public class TestDependenciesContainer {
    @Autowired
    protected DialogRepository dialogRepository;
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected DialogMapper dialogMapper;
    @Autowired
    protected MessageMapper messageMapper;
    @Autowired
    protected DialogService dialogService;
    @Autowired
    protected MessageService messageService;

    protected MockMvc mockMvc;

    @Value("${app.apiPrefix}")
    protected String apiPrefix;

    protected Dialog saveDialogInDbAndGet(DialogDto dialogDto) {
        Dialog dialog = new Dialog();
        dialog.setMember1Id(dialogDto.getMember1Id());
        dialog.setMember2Id(dialogDto.getMember2Id());
        dialog.setUnreadCount(dialogDto.getUnreadCount());
        dialogRepository.save(dialog);
        return dialog;
    }
    protected Message saveMessageInDbAndGet(MessageDto messageDto) {
        Message message = messageMapper.toEntity(messageDto);
        message.setId(null);
        messageRepository.save(message);
        return message;
    }
    protected MessageDto generateTestMessageDto(Long dialogId) {
        return MessageDto.builder()
                .authorId(1L)
                .messageText("Test message")
                .dialogId(dialogId)
                .recipientId(2L)
                .time(Instant.now())
                .build();
    }
    protected DialogDto generateTestDialogDto() {
        return DialogDto.builder()
                .id(1L)
                .member1Id(1L)
                .member2Id(2L)
                .unreadCount(1)
                .build();
    }
}
