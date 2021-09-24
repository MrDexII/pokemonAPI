package com.andrzej.RESTfullPokemonAPI.websocket;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final Set<UserSession> userSessionsList;

    public SessionService() {
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
        Set<UserSession> repeatedSessionUsers = findUser(name);
        if (!repeatedSessionUsers.isEmpty())
            userSessionsList.removeAll(repeatedSessionUsers);
    }

    public Set<UserSession> getUserSessionsList() {
        return userSessionsList;
    }

    private Set<UserSession> findUser(String name) {
        return userSessionsList.stream()
                .filter(userSession -> userSession.getUsername().equals(name))
                .collect(Collectors.toSet());
    }
}
