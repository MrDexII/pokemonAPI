package com.andrzej.RESTfullPokemonAPI.websocket.controller;

import com.andrzej.RESTfullPokemonAPI.websocket.model.GameSession;
import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSession;
import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSessionChosePokemon;
import com.andrzej.RESTfullPokemonAPI.websocket.model.WebSocketChatMessage;
import com.andrzej.RESTfullPokemonAPI.websocket.service.BattleService;
import com.andrzej.RESTfullPokemonAPI.websocket.service.UserSessionService;
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
import java.util.Optional;
import java.util.Set;

@Controller
public class WebSocketChatController {
    private final UserSessionService sessionService;
    private final SimpMessageSendingOperations messageSendingTemplate;
    private final BattleService battleService;

    private final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    public WebSocketChatController(UserSessionService sessionService,
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
        Optional<GameSession> optionalGameSession = battleService.findGameSessionById(id);
        if (optionalGameSession.isPresent()) {
            GameSession gameSession = optionalGameSession.get();
            synchronized (gameSession) {
                if (gameSession.getUserSessionsList()[0].getPokemonList() == null &&
                        gameSession.getUserSessionsList()[1].getPokemonList() == null) {
                    gameSession = battleService.setFreshUserPokemonList(gameSession);
                }
            }
            return ResponseEntity.ok(gameSession);
        }
        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/lobby.{lobbyId}")
    public void lobbyInfoExchange(@DestinationVariable String lobbyId, @Payload UserSession userSession) {
        battleService.updateBattleSession(lobbyId, userSession);
        messageSendingTemplate.convertAndSend("/topic/lobby." + lobbyId, userSession);
    }

    @MessageMapping("/lobby.changePokemon.{lobbyId}")
    public void updateLobbyWithNewPokemon(@DestinationVariable String lobbyId, @Payload UserSessionChosePokemon userSessionChangePokemon) {
        UserSession userSession = battleService.reRollPokemon(lobbyId, userSessionChangePokemon);
        messageSendingTemplate.convertAndSend("/topic/lobby." + lobbyId, userSession);
    }

    @MessageMapping("/lobby.duel.{lobbyId}")
    public void chosePokemonToDuel(@DestinationVariable String lobbyId, @Payload UserSessionChosePokemon userSessionChosePokemon) throws InterruptedException {
        GameSession gameSession = new GameSession();
        synchronized (gameSession) {
            gameSession = battleService.chosePokemonToDuel(lobbyId, userSessionChosePokemon);
            if (gameSession.getUserSessionsList()[0].getChosenPokemon() == null ||
                    gameSession.getUserSessionsList()[1].getChosenPokemon() == null
            ) return;
        }
        messageSendingTemplate.convertAndSend("/topic/lobby." + lobbyId, gameSession.getUserSessionsList()[0]);
        messageSendingTemplate.convertAndSend("/topic/lobby." + lobbyId, gameSession.getUserSessionsList()[1]);

        GameSession battle = battleService.battle(gameSession);

        messageSendingTemplate.convertAndSend("/topic/lobby." + lobbyId, battle.getUserSessionsList()[0]);
        messageSendingTemplate.convertAndSend("/topic/lobby." + lobbyId, battle.getUserSessionsList()[1]);
    }
}
