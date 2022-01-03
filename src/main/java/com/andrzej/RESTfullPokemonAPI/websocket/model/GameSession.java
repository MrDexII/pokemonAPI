package com.andrzej.RESTfullPokemonAPI.websocket.model;

public class GameSession {
    private String id;
    private UserSession[] userSessionsList;

    public GameSession() {
    }

    public GameSession(String id, UserSession userSessionId1, UserSession userSessionId2) {
        this.id = id;
        this.userSessionsList = new UserSession[]{userSessionId1, userSessionId2};
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserSession[] getUserSessionsList() {
        return userSessionsList;
    }

    public void setUserSessionsList(UserSession[] userSessionsList) {
        this.userSessionsList = userSessionsList;
    }
}
