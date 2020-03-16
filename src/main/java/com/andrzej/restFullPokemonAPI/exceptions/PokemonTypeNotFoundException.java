package com.andrzej.RESTfullPokemonAPI.exceptions;

public class PokemonTypeNotFoundException extends RuntimeException {
    public PokemonTypeNotFoundException(String message) {
        super(message);
    }
}
