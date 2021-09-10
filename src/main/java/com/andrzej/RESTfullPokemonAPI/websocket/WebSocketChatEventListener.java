package com.andrzej.RESTfullPokemonAPI.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketChatEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    private final SessionService sessionService;
    private final Logger logger = LoggerFactory.getLogger(WebSocketChatEventListener.class);

    public WebSocketChatEventListener(SimpMessageSendingOperations messageTemplate, SessionService sessionService) {
        this.messageTemplate = messageTemplate;
        this.sessionService = sessionService;
    }

    @EventListener
    public void handleWebSocketConnectionListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
        logger.info("EVENT: " + event.toString());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            sessionService.deleteUserByUserName(username);
            WebSocketChatMessage chatMessage = new WebSocketChatMessage();
            chatMessage.setType("Leave");
            chatMessage.setSender(username);
            chatMessage.setUserSessionsList(sessionService.getUserSessionsList());

            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
