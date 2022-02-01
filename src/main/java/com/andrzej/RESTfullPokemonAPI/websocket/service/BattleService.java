package com.andrzej.RESTfullPokemonAPI.websocket.service;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import com.andrzej.RESTfullPokemonAPI.websocket.model.GameSession;
import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSession;
import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSessionChosePokemon;
import com.andrzej.RESTfullPokemonAPI.websocket.utils.PokemonUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BattleService {
    private final Set<GameSession> gameSessions;
    private final UserSessionService sessionService;
    private final PokemonService pokemonService;
    private final Integer pokemonCount;

    private final Logger logger = LoggerFactory.getLogger(BattleService.class);

    public BattleService(UserSessionService sessionService, PokemonService pokemonService) {
        this.sessionService = sessionService;
        this.pokemonService = pokemonService;
//        this.pokemonCount = pokemonService.getPokemonCount();
        this.pokemonCount = 151;
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
        GameSession gameSession = checkIfGameSessionExists(sessionId);
        UserSession[] userSessionsList = gameSession.getUserSessionsList();
        if (userSessionsList[0].getSessionId().equals(userSession.getSessionId())) {
            userSessionsList[0] = userSession;
        } else {
            userSessionsList[1] = userSession;
        }
        gameSession.setUserSessionsList(userSessionsList);
        updateBattleSession(gameSession);
    }

    public void updateBattleSession(GameSession newGameSession) {
        GameSession oldGameSession = checkIfGameSessionExists(newGameSession.getId());
        this.gameSessions.remove(oldGameSession);
        this.gameSessions.add(newGameSession);
    }

    public UserSession reRollPokemon(String sessionId, UserSessionChosePokemon userSessionChangePokemon) {
        GameSession gameSession = checkIfGameSessionExists(sessionId);
        UserSession[] userSessionsList = gameSession.getUserSessionsList();
        Pokemon[] pokemonList;
        int userIndex;
        if (userSessionsList[0].getSessionId().equals(userSessionChangePokemon.userSessionId())) {
            pokemonList = userSessionsList[0].getPokemonList();
            userIndex = 0;
        } else {
            pokemonList = userSessionsList[1].getPokemonList();
            userIndex = 1;
        }
        int reRollCount = userSessionsList[userIndex].getReRollCount();
        if (reRollCount > 0) {
            userSessionsList[userIndex].decrementReRollCount();
            Set<Integer> pokemonNumbersSet = Arrays.stream(pokemonList).map(Pokemon::getNumber).collect(Collectors.toSet());
            int newPokemonNumber = drawOneNumber(pokemonNumbersSet);
            pokemonNumbersSet.remove(userSessionChangePokemon.pokemonNumber());
            pokemonNumbersSet.add(newPokemonNumber);
            Pokemon[] pokemonArray = mapNumbersToPokemonArray(pokemonNumbersSet);
            userSessionsList[userIndex].setPokemonList(pokemonArray);
        }
        gameSession.setUserSessionsList(userSessionsList);
        this.updateBattleSession(gameSession);

        return userSessionsList[userIndex];
    }

    public GameSession setFreshUserPokemonList(GameSession gameSession) {
        UserSession[] userSessions = Arrays.stream(gameSession.getUserSessionsList())
                .map(this::setFreshUserPokemonList)
                .toArray(UserSession[]::new);
        gameSession.setUserSessionsList(userSessions);
        this.updateBattleSession(gameSession);
        return gameSession;
    }

    public GameSession chosePokemonToDuel(String sessionId, UserSessionChosePokemon userSessionChosePokemon) {
        GameSession gameSession = checkIfGameSessionExists(sessionId);
        UserSession[] userSessionsList = gameSession.getUserSessionsList();
        if (userSessionsList[0].getSessionId().equals(userSessionChosePokemon.userSessionId())) {
            userSessionsList[0].setChosenPokemon(mapNumberToPokemon(userSessionChosePokemon.pokemonNumber()));
        } else {
            userSessionsList[1].setChosenPokemon(mapNumberToPokemon(userSessionChosePokemon.pokemonNumber()));
        }
        gameSession.setUserSessionsList(userSessionsList);
        this.updateBattleSession(gameSession);
        return gameSession;
    }

    public GameSession battle(GameSession gameSession) throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        int winningPokemonIndex = PokemonUtils.returnWinningPokemonIndex(
                gameSession.getUserSessionsList()[0].getChosenPokemon(),
                gameSession.getUserSessionsList()[1].getChosenPokemon());

        if (winningPokemonIndex == -1) {
            gameSession.getUserSessionsList()[0].removePokemonFromList(
                    gameSession.getUserSessionsList()[0].getChosenPokemon().get_id());
            gameSession.getUserSessionsList()[1].removePokemonFromList(
                    gameSession.getUserSessionsList()[1].getChosenPokemon().get_id());

            gameSession.getUserSessionsList()[1].setMessage("Draw");
            gameSession.getUserSessionsList()[0].setMessage("Draw");
        } else if (winningPokemonIndex == 0) {
            gameSession.getUserSessionsList()[1].removePokemonFromList(
                    gameSession.getUserSessionsList()[1].getChosenPokemon().get_id());

            gameSession.getUserSessionsList()[0].setMessage(
                    "Pokemon " + gameSession.getUserSessionsList()[winningPokemonIndex].getChosenPokemon().getName() + " won");
            gameSession.getUserSessionsList()[1].setMessage(
                    "Pokemon " + gameSession.getUserSessionsList()[winningPokemonIndex].getChosenPokemon().getName() + " won");
        } else {
            gameSession.getUserSessionsList()[0].removePokemonFromList(
                    gameSession.getUserSessionsList()[0].getChosenPokemon().get_id());

            gameSession.getUserSessionsList()[0].setMessage(
                    "Pokemon " + gameSession.getUserSessionsList()[winningPokemonIndex].getChosenPokemon().getName() + " won");
            gameSession.getUserSessionsList()[1].setMessage(
                    "Pokemon " + gameSession.getUserSessionsList()[winningPokemonIndex].getChosenPokemon().getName() + " won");
        }

        gameSession.getUserSessionsList()[0].setChosenPokemon(null);
        gameSession.getUserSessionsList()[1].setChosenPokemon(null);

        if (gameSession.getUserSessionsList()[0].getPokemonList().length == 0 &&
                gameSession.getUserSessionsList()[1].getPokemonList().length == 0) {
            gameSession.getUserSessionsList()[0].setMessage("Draw");
            gameSession.getUserSessionsList()[1].setMessage("Draw");
        } else if (gameSession.getUserSessionsList()[0].getPokemonList().length == 0) {
            gameSession.getUserSessionsList()[0].setMessage("You lost");
            gameSession.getUserSessionsList()[1].setMessage("You won");
        } else if (gameSession.getUserSessionsList()[1].getPokemonList().length == 0){
            gameSession.getUserSessionsList()[0].setMessage("You won");
            gameSession.getUserSessionsList()[1].setMessage("You lost");
        }

        updateBattleSession(gameSession);
        return gameSession;
    }

    private GameSession checkIfGameSessionExists(String sessionId) {
        Optional<GameSession> oldSession = findGameSessionById(sessionId);
        return oldSession.orElseThrow(() -> {
            throw new IllegalArgumentException("Wrong session id " + sessionId);
        });
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

    private int drawOneNumber(Set<Integer> numbersCantRoll) {
        Random random = new Random();
        while (true) {
            int value = random.nextInt(1, pokemonCount + 1);
            if (numbersCantRoll.contains(value))
                continue;
            return value;
        }
    }

    private Pokemon mapNumberToPokemon(int pokemonNumber) {
        return pokemonService.getPokemonByNumber(pokemonNumber);
    }

    private Pokemon[] mapNumbersToPokemonArray(Set<Integer> pokemonNumbers) {
        return pokemonNumbers.stream()
                .map(this::mapNumberToPokemon)
                .toArray(Pokemon[]::new);
    }
}
