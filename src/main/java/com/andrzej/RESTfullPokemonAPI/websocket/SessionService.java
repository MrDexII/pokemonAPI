package com.andrzej.RESTfullPokemonAPI.websocket;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SessionService {

    private final Set<UserSession> userSessionsList;

    public SessionService() {
        userSessionsList = new HashSet<>();
    }

    public void addUser(UserSession userSession) {
        userSessionsList.add(userSession);
    }

    public void deleteUserByUserName(String name) {
        Optional<UserSession> user = userSessionsList.stream()
                .filter(userSession -> userSession.getUsername().equals(name))
                .findFirst();
        user.ifPresent(userSessionsList::remove);
    }

    public Set<UserSession> getUserSessionsList() {
        return userSessionsList;
    }
}
