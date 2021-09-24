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
        return webSocketChatMessage;
    }

    @MessageMapping("/chat.sendToUser")
    public void sendToUser(@Payload WebSocketChatMessage webSocketChatMessage) {
        WebSocketChatMessage chatMessage = new WebSocketChatMessage();
        chatMessage.setType("message");
        chatMessage.setContent("User " + webSocketChatMessage.getSender() + " want's to play");

        if (webSocketChatMessage.getType().equals("battleRequest")) {
            messageSendingTemplate.convertAndSend("/topic/users-user" + webSocketChatMessage.getContent(), chatMessage);
        }
    }
}
