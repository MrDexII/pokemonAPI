package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.exceptions.PokemonNotFoundException;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokemonModelAssembler pokemonModelAssembler;
    private final PagedResourcesAssembler<Pokemon> pagedResourcesAssembler;
    private final PokemonRepository pokemonRepository;


    @Autowired
    public PokemonService(PokemonModelAssembler pokemonModelAssembler,
                          PagedResourcesAssembler<Pokemon> pagedResourcesAssembler,
                          PokemonRepository pokemonRepository) {
        this.pokemonModelAssembler = pokemonModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pokemonRepository = pokemonRepository;
    }

    public EntityModel<Pokemon> createPokemon(Pokemon pokemon) {
        return pokemonModelAssembler.toModel(pokemonRepository.save(pokemon));
    }

    public PagedModel<EntityModel<Pokemon>> getAllPokemons(Pageable pageable) {
//        if (pageable.getPageSize() >= 10){
//            pageable = new PokemonPageable(pageable);
//        }
//        pokemonModelAssembler.setPageable(pageable);

        return pagedResourcesAssembler.toModel(pokemonRepository.findAll(pageable), pokemonModelAssembler);
    }

    public EntityModel<Pokemon> getPokemonById(String id) {
        return pokemonModelAssembler.toModel(pokemonRepository.findById(id).orElseThrow(() ->
                new PokemonNotFoundException("Pokemon with id: " + id + " not found")));
    }

    public EntityModel<Pokemon> getPokemonByName(String name) {
        return pokemonModelAssembler.toModel(pokemonRepository.findByPokemonName(name).orElseThrow(() ->
                new PokemonNotFoundException("Pokemon with name: " + name + " not found")));
    }

    public EntityModel<Pokemon> updatePokemon(String id, Pokemon pokemon) {
        Pokemon pokemon1 = pokemonRepository.findById(id).orElseThrow(() ->
                new PokemonNotFoundException("Pokemon with id: " + id + " not found"));
        pokemon.setId(pokemon1.getId());
        return pokemonModelAssembler.toModel(pokemonRepository.save(pokemon));
    }

    public void deletePokemon(String id) {
        pokemonRepository.delete(pokemonRepository.findById(id).orElseThrow(() ->
                new PokemonNotFoundException("Pokemon with id: " + id + " not found")));
    }

    public boolean isPokemonPresent(String pokemonName) {
        return pokemonRepository.findByPokemonName(pokemonName).isPresent();
    }
}
