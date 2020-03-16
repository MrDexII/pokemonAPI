package com.andrzej.RESTfullPokemonAPI.exceptions;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException(String message) {
        super(message);
    }
}
