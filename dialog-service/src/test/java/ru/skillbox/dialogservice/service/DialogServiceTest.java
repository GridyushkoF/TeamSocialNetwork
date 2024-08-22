package ru.skillbox.dialogservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.repository.DialogRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class DialogServiceTest {

    @Autowired
    protected DialogService dialogService;

    @Autowired
    protected DialogRepository dialogRepository;

    @BeforeEach
    void setUp() {
        Dialog dialog = Dialog.builder()
                .id(1L)
                .member1Id(1L)
                .member2Id(2L)
                .unreadCount(1)
                .build();
        dialogRepository.save(dialog);
    }

    @Test
    void testGetDialogs() {
        String sort = "unreadCount,desc";
        Page<DialogDto> dialogs = dialogService.getDialogs(0, sort, 1L);
        assertNotNull(dialogs);
        assertEquals(1, dialogs.getTotalElements());
        assertEquals(1, dialogs.getContent().get(0).getId());
        assertThrows(IndexOutOfBoundsException.class, () -> dialogs.getContent().get(1));
    }

    @Test
    void testGetDialog() {
        DialogDto dialogDto = dialogService.getDialog(1L, 2L);
        assertNotNull(dialogDto);
        assertEquals(1L, dialogDto.getId());
        assertEquals(1, dialogDto.getUnreadCount());
        assertEquals(1, dialogDto.getMember1Id());
        assertEquals(2, dialogDto.getMember2Id());
    }

    @Test
    void testUpdateDialog() {
        DialogDto dialogDto = dialogService.getDialog(1L, 2L);
        assertEquals(1, dialogDto.getUnreadCount());
        dialogDto = dialogService.updateDialog(1L, 1L);
        assertEquals(0, dialogDto.getUnreadCount());
    }
}