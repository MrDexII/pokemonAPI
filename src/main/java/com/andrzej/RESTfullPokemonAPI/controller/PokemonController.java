package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;
    private final PokemonModelAssembler pokemonModelAssembler;


    @Autowired
    public PokemonController(PokemonService pokemonService, PokemonModelAssembler pokemonModelAssembler) {
        this.pokemonService = pokemonService;
        this.pokemonModelAssembler = pokemonModelAssembler;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPokemon(@RequestBody Pokemon pokemon) {
        EntityModel<Pokemon> entityModel = pokemonModelAssembler.toModel(pokemonService.createPokemon(pokemon));

        return ResponseEntity.created(
                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/")
    public CollectionModel<EntityModel<Pokemon>> getAllPokemons() {
        List<EntityModel<Pokemon>> allPokemons = pokemonService.getAllPokemons().stream()
                .map(pokemonModelAssembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(allPokemons,
                linkTo(methodOn(PokemonController.class).getAllPokemons()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Pokemon> getPokemonById(@PathVariable("id") String id) {
        return pokemonModelAssembler.toModel(pokemonService.getPokemonById(id));
    }

    @GetMapping("/find")
    public EntityModel<Pokemon> getPokemonByName(@RequestParam("name") String name) {
        Pokemon pokemonByName = pokemonService.getPokemonByName(name);
        return pokemonModelAssembler.toModel(pokemonByName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePokemon(@PathVariable("id") String id, @RequestBody Pokemon pokemon) {
        EntityModel<Pokemon> entityModel = pokemonModelAssembler.toModel(pokemonService.updatePokemon(id, pokemon));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePokemon(@PathVariable("id") String id) {
        pokemonService.deletePokemon(id);
        return ResponseEntity.noContent().build();
    }
}
