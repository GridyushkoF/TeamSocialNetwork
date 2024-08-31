package ru.skillbox.dialogservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.skillbox.commonlib.dto.statistics.CountDto;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.enums.MessageStatus;
import ru.skillbox.dialogservice.service.DialogService;
import ru.skillbox.dialogservice.service.MessageService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@WithUserDetails(value = "admin@socialnetwork.com")
class DialogControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private DialogService dialogService;
    @MockBean
    private MessageService messageService;

    @Configuration
    @ComponentScan(basePackageClasses = {DialogController.class})
    public static class TestConfig {
    }

    private DialogDto dialogDto;
    private final List<DialogDto> dialogDtoList = new ArrayList<>();

    private MessageDto messageDto = new MessageDto();
    private final List<MessageDto> messageDtoList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        dialogDto = DialogDto.builder()
                .id(1L)
                .unreadCount(1)
                .member1Id(1L)
                .member2Id(2L)
                .build();
        dialogDtoList.add(dialogDto);

        messageDto = MessageDto.builder()
                .status(MessageStatus.SENT)
                .dialogId(1L)
                .authorId(1L)
                .recipientId(2L)
                .messageText("test message")
                .time(Instant.now())
                .build();
        messageDtoList.add(messageDto);
    }


    @Test
    @DisplayName("test update dialog by id, dialog is updated")
    void testUpdateDialog() throws Exception {
        Mockito.when(dialogService.updateDialog(1L, 1L))
                .thenReturn(dialogDto);
        mvc.perform(
                        put("/api/v1/dialogs/1").header("id", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(dialogDto.getUnreadCount()))));

        Mockito.verify(dialogService, Mockito.times(1))
                .updateDialog(1L, 1L);
    }

    @Test
    @DisplayName("test get dialogs, return dialogs")
    void testGetDialogs() throws Exception {
        String sort = "unreadCount,desc";
        String[] sorts = sort.split(",");
        Pageable nextPage = PageRequest.of(0, Integer.MAX_VALUE,
                Sort.by(Sort.Direction.fromString(sorts[1]), sorts[0]));
        Page<DialogDto> dialogDtoPage = new PageImpl<>(dialogDtoList, nextPage, dialogDtoList.size());
        Mockito.when(dialogService.getDialogs(0, sort,2L))
                .thenReturn(dialogDtoPage);
        mvc.perform(
                get("/api/v1/dialogs").header("id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(dialogDto.getUnreadCount()))));

        Mockito.verify(dialogService, Mockito.times(1))
                .getDialogs(0, sort, 2L);
    }

    @Test
    @DisplayName("test get unread dialogs, return unread count 1")
    void testGetUnread() throws Exception {

        Mockito.when(messageService.getUnread(1L))
                .thenReturn(new CountDto(messageDtoList.size()));
        mvc.perform(
                        get("/api/v1/dialogs/unread").header("id", 1)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"count\":1")));

        Mockito.verify(messageService, Mockito.times(1))
                .getUnread(1L);
    }

    @Test
    @DisplayName("test get messages, return message")
    void testGetMessages() throws Exception {
        String sort = "time,asc";
        String[] sorts = sort.split(",");
        Pageable nextPage = PageRequest.of(0, Integer.MAX_VALUE,
                Sort.by(Sort.Direction.fromString(sorts[1]), sorts[0]));
        Page<MessageDto> messageDtoPage = new PageImpl<>(messageDtoList, nextPage, messageDtoList.size());
        Mockito.when(messageService.getMessages(1L, 2L, 0, sort))
                .thenReturn(messageDtoPage);
        mvc.perform(
                        get("/api/v1/dialogs/messages?recipientId=2").header("id", 1)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(messageDto.getMessageText())));

        Mockito.verify(messageService, Mockito.times(1))
                .getMessages(1L, 2L, 0, sort);
    }

    @Test
    @DisplayName("test get dialog, return dialog")
    void testGetDialog() throws Exception {
        Mockito.when(dialogService.getDialog(1L, 2L))
                .thenReturn(dialogDto);
        mvc.perform(
                        get("/api/v1/dialogs/recipientId/2").header("id", 1)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(dialogDto.getUnreadCount()))));

        Mockito.verify(dialogService, Mockito.times(1))
                .getDialog(1L, 2L);
    }
}