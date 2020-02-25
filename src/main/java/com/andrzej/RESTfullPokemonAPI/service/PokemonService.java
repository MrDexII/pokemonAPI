package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.exceptions.PokemonNotFoundException;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public Pokemon createPokemon(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    public Page<Pokemon> getAllPokemons(Pageable pageable) {
        return pokemonRepository.findAll(pageable);
    }

    public Pokemon getPokemonById(String id) {
        return pokemonRepository.findById(id).orElseThrow(() -> new PokemonNotFoundException("Pokemon with id: " + id + " not found"));
    }

    public Pokemon updatePokemon(String id, Pokemon pokemon) {
        Pokemon pokemon1 = pokemonRepository.findById(id).orElseThrow(() -> new PokemonNotFoundException("Pokemon with id: " + id + " not found"));
        pokemon.setId(pokemon1.getId());
        return pokemonRepository.save(pokemon);
    }

    public void deletePokemon(String id) {
        pokemonRepository.delete(pokemonRepository.findById(id).orElseThrow(() -> new PokemonNotFoundException("Pokemon with id: " + id + " not found")));
    }

    public Pokemon getPokemonByName(String name) {
        return pokemonRepository.findByPokemonName(name);
    }
}
