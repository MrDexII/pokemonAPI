package com.andrzej.RESTfullPokemonAPI.websocket;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BattleService {
    private Set<GameSession> gameSessions;
    private final SessionService sessionService;

    public BattleService(SessionService sessionService) {
        this.sessionService = sessionService;
        this.gameSessions = new HashSet<>();
    }


    public GameSession createUserUserSession(String userSessionId, String userSessionIdToSend) {
        String id = new ObjectId().toHexString();
        GameSession gameSession = new GameSession(
                id,
                sessionService.getUserSessionById(userSessionId),
                sessionService.getUserSessionById(userSessionIdToSend));
        gameSessions.add(gameSession);
        return gameSession;
    }

    public Optional<GameSession> findById(String id) {
        return gameSessions.stream().filter(gameSession -> gameSession.getId().equals(id)).findFirst();
    }

    public boolean isPresent(String id) {
        Optional<GameSession> session = findById(id);
        return session.isPresent();
    }

    public Set<GameSession> findAll() {
        return gameSessions;
    }

    public void updateBattleSession(GameSession gameSession) {
        Optional<GameSession> oldSession = this.gameSessions.stream().filter(session -> session.getId().equals(gameSession.getId())).findFirst();
        oldSession.ifPresent(session -> this.gameSessions.remove(session));
        this.gameSessions.add(gameSession);
    }
}
