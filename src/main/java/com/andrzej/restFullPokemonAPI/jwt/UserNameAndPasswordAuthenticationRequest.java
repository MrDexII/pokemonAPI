package com.andrzej.restFullPokemonAPI.jwt;

public class UserNameAndPasswordAuthenticationRequest {

    private String userName;
    private String userPassword;

    public UserNameAndPasswordAuthenticationRequest() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
