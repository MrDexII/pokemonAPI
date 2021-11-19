package com.andrzej.RESTfullPokemonAPI.websocket.service;

import com.andrzej.RESTfullPokemonAPI.websocket.model.UserSession;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSessionService {

    private final Set<UserSession> userSessionsList;

    public UserSessionService() {
        userSessionsList = new HashSet<>();
    }

    public void addUser(UserSession userSession) {
        if (findUser(userSession.getUsername()).isEmpty()) {
            userSessionsList.add(userSession);
        } else {
            throw new IllegalArgumentException("User " + userSession.getUsername() + " already connected");
        }
    }

    public void deleteUserByUserName(String name) {
        userSessionsList.removeIf(userSession -> userSession.getUsername().equals(name));
    }

    public Set<UserSession> getUserSessionsList() {
        return userSessionsList;
    }

    private Set<UserSession> findUser(String name) {
        return userSessionsList.stream()
                .filter(userSession -> userSession.getUsername().equals(name))
                .collect(Collectors.toSet());
    }

    public UserSession getUserSessionById(String userSessionId) {
        Optional<UserSession> session = userSessionsList.stream()
                .filter(userSession -> userSession.getSessionId().equals(userSessionId)).findFirst();
        return session.orElseThrow(() -> new IllegalArgumentException("User session: " + userSessionId + " not Found"));
    }
}
