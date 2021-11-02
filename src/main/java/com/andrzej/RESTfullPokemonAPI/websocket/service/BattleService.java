package com.andrzej.RESTfullPokemonAPI.websocket.service;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import com.andrzej.RESTfullPokemonAPI.websocket.model.GameSession;
import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSession;
import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSessionChangePokemon;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BattleService {
    private final Set<GameSession> gameSessions;
    private final SessionService sessionService;
    private final PokemonService pokemonService;
    private final Integer pokemonCount;

    public BattleService(SessionService sessionService, PokemonService pokemonService) {
        this.sessionService = sessionService;
        this.pokemonService = pokemonService;
        this.pokemonCount = pokemonService.getPokemonCount();
//        this.pokemonCount = 151;
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

    public Optional<GameSession> findGameSessionById(String id) {
        return gameSessions.stream().filter(gameSession -> gameSession.getId().equals(id)).findFirst();
    }

    public Set<GameSession> findAll() {
        return gameSessions;
    }

    public void updateBattleSession(String sessionId, UserSession userSession) {
        Optional<GameSession> optionalGameSession = findGameSessionById(sessionId);
        GameSession gameSession = optionalGameSession.orElseThrow(() ->
                new IllegalArgumentException("wrong game session id: " + sessionId));
        UserSession[] userSessionsList = gameSession.getUserSessionsList();
        if (userSessionsList[0].getSessionId().equals(userSession.getSessionId())) {
            userSessionsList[0] = userSession;
        } else {
            userSessionsList[1] = userSession;
        }
        gameSession.setUserSessionsList(userSessionsList);

        Optional<GameSession> oldSession = this.gameSessions.stream()
                .filter(session -> session.getId().equals(gameSession.getId()))
                .findFirst();
        oldSession.ifPresent(this.gameSessions::remove);
        this.gameSessions.add(gameSession);
    }

    public void updateBattleSession(GameSession gameSession) {
        Optional<GameSession> oldGameSession = this.gameSessions.stream()
                .filter(gameSession1 -> gameSession1.getId().equals(gameSession.getId()))
                .findFirst();
        oldGameSession.ifPresent(this.gameSessions::remove);
        this.gameSessions.add(gameSession);
    }

    public UserSession updateBattleSession(String id, UserSessionChangePokemon userSessionChangePokemon) {
        Optional<GameSession> oldSession = this.gameSessions.stream()
                .filter(session -> session.getId().equals(id))
                .findFirst();
        oldSession.ifPresentOrElse(this.gameSessions::remove,
                () -> {
                    throw new IllegalArgumentException("Wrong session id " + id);
                });
        GameSession newGameSession = oldSession.get();
        UserSession[] userSessionsList = oldSession.get().getUserSessionsList();
        Pokemon[] pokemonList;
        int userIndex;
        if (userSessionsList[0].getSessionId().equals(userSessionChangePokemon.sessionId())) {
            pokemonList = userSessionsList[0].getPokemonList();
            userIndex = 0;
        } else {
            pokemonList = userSessionsList[1].getPokemonList();
            userIndex = 1;
        }
        Set<Integer> pokemonNumbersSet = Arrays.stream(pokemonList).map(Pokemon::getNumber).collect(Collectors.toSet());
        int newPokemonNumber = drawOneNumber(pokemonNumbersSet);
        pokemonNumbersSet.remove(userSessionChangePokemon.pokemonNumberToChange());
        pokemonNumbersSet.add(newPokemonNumber);
        Pokemon[] pokemonArray = mapNumbersToPokemonArray(pokemonNumbersSet);

        userSessionsList[userIndex].setPokemonList(pokemonArray);
        newGameSession.setUserSessionsList(userSessionsList);
        this.updateBattleSession(newGameSession);

        return userSessionsList[userIndex];
    }

    private UserSession setFreshUserPokemonList(UserSession userSession) {
        Set<Integer> pokemonNumbers = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            pokemonNumbers.add(drawOneNumber(pokemonNumbers));
        }
        Pokemon[] pokemonArray = mapNumbersToPokemonArray(pokemonNumbers);
        userSession.setPokemonList(pokemonArray);
        return userSession;
    }


    public GameSession setFreshUserPokemonList(GameSession gameSession) {
        UserSession[] userSessions = Arrays.stream(gameSession.getUserSessionsList())
                .map(this::setFreshUserPokemonList)
                .toArray(UserSession[]::new);
        gameSession.setUserSessionsList(userSessions);
        this.updateBattleSession(gameSession);
        return gameSession;
    }

    private int drawOneNumber(Set<Integer> numbersCantRoll) {
        Random random = new Random();
        while (true) {
            int value = random.nextInt(1, pokemonCount + 1);
            if (numbersCantRoll.contains(value))
                continue;
            return value;
        }
    }

    private Pokemon[] mapNumbersToPokemonArray(Set<Integer> pokemonNumbers) {
        return pokemonNumbers.stream()
                .map(pokemonService::getPokemonByNumber)
                .toArray(Pokemon[]::new);
    }
}
