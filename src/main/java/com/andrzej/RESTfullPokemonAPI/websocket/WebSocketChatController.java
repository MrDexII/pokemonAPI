package com.andrzej.RESTfullPokemonAPI.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {
    private final SessionService sessionService;

    public WebSocketChatController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/andrewChat")
    public WebSocketChatMessage sendMessage(@Payload WebSocketChatMessage webSocketChatMessage) {
        return webSocketChatMessage;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/andrewChat")
    public WebSocketChatMessage newUser(@Payload WebSocketChatMessage webSocketChatMessage,
                                        SimpMessageHeaderAccessor headerAccessor) {
        sessionService.addUser(new UserSession(headerAccessor.getSessionId(), webSocketChatMessage.getSender()));
        headerAccessor.getSessionAttributes().put("username", webSocketChatMessage.getSender());
        webSocketChatMessage.setUserSessionsList(sessionService.getUserSessionsList());
        return webSocketChatMessage;
    }
}
