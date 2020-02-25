package com.andrzej.restFullPokemonAPI.exceptions;

public class PokemonTypeNotFoundException extends RuntimeException {
    public PokemonTypeNotFoundException(String message) {
        super(message);
    }
}
