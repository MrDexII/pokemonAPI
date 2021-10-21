package com.andrzej.RESTfullPokemonAPI.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Controller
public class WebSocketChatController {
    private final SessionService sessionService;
    private final SimpMessageSendingOperations messageSendingTemplate;
    private final BattleService battleService;

    private final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    public WebSocketChatController(SessionService sessionService,
                                   SimpMessageSendingOperations messageSendingTemplate,
                                   BattleService battleService) {
        this.sessionService = sessionService;
        this.messageSendingTemplate = messageSendingTemplate;
        this.battleService = battleService;
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
        UserSession userSession = new UserSession(headerAccessor.getSessionId(), principal.getName());
        sessionService.addUser(userSession);
        webSocketChatMessage.setUserSessionsList(sessionService.getUserSessionsList());
        webSocketChatMessage.setUserSessionId(headerAccessor.getSessionId());
        webSocketChatMessage.setSender(principal.getName());
        webSocketChatMessage.setColor(userSession.getColor());
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
                GameSession gameSession = battleService.createUserUserSession(webSocketChatMessage.getUserSessionId(),
                        userSessionIdToSend);

                UserSession userSession1 = sessionService.getUserSessionById(webSocketChatMessage.getUserSessionId());
                UserSession userSession2 = sessionService.getUserSessionById(userSessionIdToSend);

                WebSocketChatMessage chatMessage = new WebSocketChatMessage();
                chatMessage.setType("positiveBattleRequest");
                chatMessage.setContent(gameSession.getId());
                chatMessage.setSender("server");
                chatMessage.setUserSessionsList(Set.of(userSession1, userSession2));

                messageSendingTemplate.convertAndSend(
                        "/topic/users-user" + webSocketChatMessage.getUserSessionId(),
                        chatMessage);
                messageSendingTemplate.convertAndSend(
                        "/topic/users-user" + userSessionIdToSend,
                        chatMessage);
            }
        }
    }

    @GetMapping("/gameSession/{id}")
    public ResponseEntity<GameSession> checkIsGameSessionExists(@PathVariable("id") String id) {
        boolean isSessionPresent = battleService.isPresent(id);
        Optional<GameSession> byId = battleService.findById(id);
        if (isSessionPresent) {
            return ResponseEntity.ok(byId.get());
        } else
            return ResponseEntity.notFound().build();
    }


    @MessageMapping("/lobby.{id}")
    public void lobbyInfoExchange(@DestinationVariable String id, @Payload UserSession userSession) {
        Optional<GameSession> optionalGameSession = battleService.findById(id);
        GameSession gameSession = optionalGameSession.orElseThrow(() ->
                new IllegalArgumentException("wrong game session id: " + id));
        UserSession[] userSessionsList = gameSession.getUserSessionsList();
        if (Objects.equals(userSessionsList[0].getSessionId(), userSession.getSessionId())) {
            userSessionsList[0] = userSession;
        } else {
            userSessionsList[1] = userSession;
        }
        gameSession.setUserSessionsList(userSessionsList);
        battleService.updateBattleSession(gameSession);
        messageSendingTemplate.convertAndSend("/topic/lobby." + id, userSession);
    }
}
