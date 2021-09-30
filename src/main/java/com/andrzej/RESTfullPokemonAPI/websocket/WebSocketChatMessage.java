package com.andrzej.RESTfullPokemonAPI.websocket;

import java.util.Set;

public class WebSocketChatMessage {
    private String userSessionId;
    private String type;
    private String content;
    private String sender;
    private Set<UserSession> userSessionsList;

    public Set<UserSession> getUserSessionsList() {
        return userSessionsList;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    public void setUserSessionsList(Set<UserSession> userSessionsList) {
        this.userSessionsList = userSessionsList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
