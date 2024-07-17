package ru.skillbox.dialogservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import ru.skillbox.commondto.account.AccountDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DialogDto {
    private Long id;
    private Long unreadCount;
    private AccountDto conversationPartner;
    private MessageDto lastMessage;
}
