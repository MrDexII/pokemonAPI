package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;
    private final PokemonModelAssembler pokemonModelAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;


    @Autowired
    public PokemonController(PokemonService pokemonService, PokemonModelAssembler pokemonModelAssembler, PagedResourcesAssembler pagedResourcesAssembler) {
        this.pokemonService = pokemonService;
        this.pokemonModelAssembler = pokemonModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPokemon(@RequestBody Pokemon pokemon) {
        EntityModel<Pokemon> entityModel = pokemonModelAssembler.toModel(pokemonService.createPokemon(pokemon));

        return ResponseEntity.created(
                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/all")
    public ResponseEntity<PagedModel<Pokemon>> getAllPokemons(Pageable pageable) {
        Page<Pokemon> allPokemons = pokemonService.getAllPokemons(pageable);
        PagedModel pagedModel = pagedResourcesAssembler.toModel(allPokemons, pokemonModelAssembler);

        return ResponseEntity.ok(pagedModel);
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
