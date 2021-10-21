package com.andrzej.RESTfullPokemonAPI.websocket;

import java.util.Random;

public class UserSession {

    private String sessionId;
    private String username;
    private RGBColor color;
    private Boolean ready;

    public UserSession(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.color = setColor();
    }

    private RGBColor setColor() {
        Random random = new Random();
        int read = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new RGBColor(read, green, blue);
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

    public RGBColor getColor() {
        return color;
    }

    public Boolean isReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "sessionId='" + sessionId + '\'' +
                ", username='" + username + '\'' +
                ", color=" + color +
                ", ready=" + ready +
                '}';
    }
}
