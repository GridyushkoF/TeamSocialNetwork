package ru.skillbox.dialogservice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.dialogservice.model.dto.ConversationMessageDto;
import ru.skillbox.dialogservice.model.enums.MessageType;
import ru.skillbox.dialogservice.service.MessageService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketHandlerImpl implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final MessageService service;

    private final Map<Integer, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.put(session.getPrincipal().getName().hashCode(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ConversationMessageDto messageDto = mapper.readValue(message.getPayload().toString(),
                ConversationMessageDto.class);
        if (messageDto.getType() == MessageType.MESSAGE) {
            WebSocketSession socketSession = sessions.get(messageDto.getRecipientId());
            if (socketSession != null) {
                socketSession.sendMessage(message);
            }
            service.saveMessage(messageDto.getData());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
