package com.andrzej.RESTfullPokemonAPI.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketChatController {
    private final SessionService sessionService;
    private final SimpMessageSendingOperations messageSendingTemplate;

    public WebSocketChatController(SessionService sessionService, SimpMessageSendingOperations messageSendingTemplate) {
        this.sessionService = sessionService;
        this.messageSendingTemplate = messageSendingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/gameChat")
    public WebSocketChatMessage sendMessage(@Payload WebSocketChatMessage webSocketChatMessage) {
        return webSocketChatMessage;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/gameChat")
    public WebSocketChatMessage newUser(@Payload WebSocketChatMessage webSocketChatMessage,
                                        SimpMessageHeaderAccessor headerAccessor,
                                        Principal principal) {
        sessionService.addUser(new UserSession(headerAccessor.getSessionId(), principal.getName()));
        webSocketChatMessage.setUserSessionsList(sessionService.getUserSessionsList());
        webSocketChatMessage.setUserSessionId(headerAccessor.getSessionId());
        webSocketChatMessage.setSender(principal.getName());
        return webSocketChatMessage;
    }

    @MessageMapping("/chat.sendRequestForPlayToUser")
    public void sendToUser(@Payload WebSocketChatMessage webSocketChatMessage,
                           SimpMessageHeaderAccessor headerAccessor,
                           Principal principal) {

        String userSessionIdToSend = webSocketChatMessage.getUserSessionId();
        webSocketChatMessage.setUserSessionId(headerAccessor.getSessionId());

        switch (webSocketChatMessage.getType()) {
            case "battleRequest", "negativeBattleRequest" -> messageSendingTemplate.convertAndSend(
                    "/topic/users-user" + userSessionIdToSend,
                    webSocketChatMessage);
            case "positiveBattleRequest" -> {
                messageSendingTemplate.convertAndSend(
                        "/topic/users-user" + webSocketChatMessage.getUserSessionId(),
                        webSocketChatMessage);
                messageSendingTemplate.convertAndSend(
                        "/topic/users-user" + userSessionIdToSend,
                        webSocketChatMessage);
            }
        }
    }
}
