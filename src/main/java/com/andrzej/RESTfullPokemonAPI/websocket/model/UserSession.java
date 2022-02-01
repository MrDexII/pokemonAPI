package com.andrzej.RESTfullPokemonAPI.websocket.model;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class UserSession {

    private String sessionId;
    private String username;
    private RGBColor color;
    private Boolean ready;
    private Pokemon[] pokemonList;
    private int reRollCount;
    private Pokemon chosenPokemon;
    private String message;

    public UserSession(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.color = setColor();
        this.reRollCount = 3;
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

    public Pokemon[] getPokemonList() {
        return pokemonList;
    }

    public void setPokemonList(Pokemon[] pokemonList) {
        this.pokemonList = pokemonList;
    }

    public int getReRollCount() {
        return reRollCount;
    }

    public void decrementReRollCount() {
        this.reRollCount = this.reRollCount - 1;
    }

    public Pokemon getChosenPokemon() {
        return chosenPokemon;
    }

    public void setChosenPokemon(Pokemon chosenPokemon) {
        this.chosenPokemon = chosenPokemon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void removePokemonFromList(String id) {
        Pokemon[] pokemons = Arrays.stream(this.pokemonList)
                .filter(pokemon -> !pokemon.get_id().equals(id))
                .toArray(Pokemon[]::new);
        this.setPokemonList(pokemons);
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "sessionId='" + sessionId + '\'' +
                ", username='" + username + '\'' +
                ", color=" + color +
                ", ready=" + ready +
                ", pokemonList=" + Arrays.toString(pokemonList) +
                ", reRollCount=" + reRollCount +
                ", chosenPokemon=" + chosenPokemon +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession that = (UserSession) o;
        return reRollCount == that.reRollCount && Objects.equals(sessionId, that.sessionId) && Objects.equals(username, that.username) && Objects.equals(color, that.color) && Objects.equals(ready, that.ready) && Arrays.equals(pokemonList, that.pokemonList) && Objects.equals(chosenPokemon, that.chosenPokemon) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sessionId, username, color, ready, reRollCount, chosenPokemon, message);
        result = 31 * result + Arrays.hashCode(pokemonList);
        return result;
    }
}
