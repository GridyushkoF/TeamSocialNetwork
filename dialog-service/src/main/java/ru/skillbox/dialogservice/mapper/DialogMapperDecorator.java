package ru.skillbox.dialogservice.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.dialogservice.exception.MessageNotFoundException;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.model.entity.Message;
import ru.skillbox.dialogservice.repository.MessageRepository;

import java.util.Optional;


@Component
public class DialogMapperDecorator implements DialogMapper{

    @Autowired
    @Qualifier("delegate")
    private DialogMapper delegate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageMapper messageMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public DialogDto dialogToDialogDto(Dialog dialog, Long companionId,HttpServletRequest request)  throws Exception {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        DialogDto dialogDto = delegate.dialogToDialogDto(dialog);
        AccountDto partnerAccountDto = getAccountDtoByIdFromUserService(companionId,request);
        dialogDto.setConversationPartner(partnerAccountDto);
        MessageDto lastMessageDto = getLastMessageDto(currentAuthUserId,companionId);
        dialogDto.setLastMessage(lastMessageDto);
        return dialogDto;
    }
    private AccountDto getAccountDtoByIdFromUserService(Long accountId, HttpServletRequest request) throws Exception {
        String url = "http://localhost:9090/api/v1/account/{accountId}";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(request.getHeader("Authorization").substring(7));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class,
                accountId
        );
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), AccountDto.class);
    }
    private MessageDto getLastMessageDto(Long member1,Long member2) {
        Optional<Message> messageOptional = messageRepository.getLastMessageByMembers(member1,member2);
        if(messageOptional.isPresent()) {
            return messageMapper.dialogToDialogDto(messageOptional.get());
        }
        throw new MessageNotFoundException();
    }

    @Override
    public DialogDto dialogToDialogDto(Dialog dialog) {
        return DialogDto.builder()
                .id(dialog.getId())
                .unreadCount(dialog.getUnreadCount())
                .build();
    }
}
