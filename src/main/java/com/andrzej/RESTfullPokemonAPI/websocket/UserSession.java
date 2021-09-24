package com.andrzej.RESTfullPokemonAPI.websocket;

public class UserSession {

    private String sessionId;
    private String username;

    public UserSession(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
    }

    public UserSession() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "sessionId='" + sessionId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
