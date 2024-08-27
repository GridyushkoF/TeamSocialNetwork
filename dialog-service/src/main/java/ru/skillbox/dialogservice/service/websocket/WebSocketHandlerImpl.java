package ru.skillbox.dialogservice.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.dialogservice.model.dto.ConversationMessageDto;
import ru.skillbox.dialogservice.model.enums.MessageType;
import ru.skillbox.dialogservice.service.MessageService;
import ru.skillbox.dialogservice.service.feign.DialogFeignClient;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class WebSocketHandlerImpl implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final MessageService service;
    private final DialogFeignClient dialogFeignClient;
    private final Map<Long, WebSocketSession> sessions = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(Long.parseLong(Objects.requireNonNull(session.getPrincipal()).getName()), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ConversationMessageDto messageDto = mapper.readValue(message.getPayload().toString(),
                ConversationMessageDto.class);
        if (messageDto.getType() == MessageType.MESSAGE) {
            service.saveMessage(messageDto.getData());
            session.sendMessage(message);

            WebSocketSession socketSession = sessions.get(messageDto.getRecipientId());
            if (socketSession != null) {
                try {
                    socketSession.sendMessage(message);
                } catch (Exception e) {
                    sessions.remove(messageDto.getRecipientId());
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        //Realize later
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        dialogFeignClient.closeConnection(Long.parseLong(session.getPrincipal().getName()));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

//
//    private void findUserByIdAndChangeStatusToFalse(Long id) {
//        dialogFeignClient.closeConnection(id);
//
//    }


}
