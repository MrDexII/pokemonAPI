package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ResponseEntity<?> createPokemon(Pokemon pokemon) {
        String pokemonName = pokemon.getPokemonName();
        Optional<Pokemon> byPokemonName = pokemonRepository.findByPokemonName(pokemonName);

        if (byPokemonName.isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pokemon with name: " + pokemonName + " already exists");

        EntityModel<Pokemon> entityModel = pokemonModelAssembler.toModel(pokemonRepository.save(pokemon));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    public ResponseEntity<?> getAllPokemons(Pageable pageable) {
        Page<Pokemon> pokemons = pokemonRepository.findAll(pageable);

        if (pokemons.getContent().size() == 0)
            return ResponseEntity.noContent().build();

        PagedModel<EntityModel<Pokemon>> entityModels = pagedResourcesAssembler.toModel(pokemons, pokemonModelAssembler);
        return ResponseEntity.ok(entityModels);
    }

    public ResponseEntity<?> getPokemonById(String id) {
        Optional<Pokemon> pokemon = this.pokemonRepository.findById(id);
        return pokemon.isPresent() ?
                ResponseEntity.ok(pokemonModelAssembler.toModel(pokemon.get())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon with id: " + id + " not exists");
    }

    public ResponseEntity<?> getPokemonByName(String name) {
        Optional<Pokemon> byPokemonName = pokemonRepository.findByPokemonName(name);
        return byPokemonName.isPresent() ?
                ResponseEntity.ok(pokemonModelAssembler.toModel(byPokemonName.get())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon with name: " + name + " not exists");

    }

    public ResponseEntity<?> updatePokemon(String id, Pokemon pokemon) {
        Optional<Pokemon> pokemonById = pokemonRepository.findById(id);
        if (!pokemonById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon with id: " + id + " not exists");
        }
        pokemon.setId(id);
        Pokemon save = pokemonRepository.save(pokemon);
        return ResponseEntity.ok(pokemonModelAssembler.toModel(save));
    }

    public ResponseEntity<?> deletePokemon(String id) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(id);
        if (!pokemon.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon with id: " + id + " not exists");
        }
        pokemonRepository.delete(pokemon.get());
        return ResponseEntity.noContent().build();
    }
}
