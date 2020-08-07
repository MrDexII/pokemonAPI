package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createPokemon(@RequestBody Pokemon pokemon) {
        String pokemonName = pokemon.getPokemonName();

        if (pokemonService.isPokemonPresent(pokemonName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pokemon with name: " + pokemonName + " already exists");
        }

        EntityModel<Pokemon> entityModel = pokemonService.createPokemon(pokemon);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<Pokemon>>> getAllPokemons(Pageable pageable) {
        return ResponseEntity.ok(pokemonService.getAllPokemons(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pokemon>> getPokemonById(@PathVariable("id") String id) {
        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }

    @GetMapping("/find")
    public ResponseEntity<EntityModel<Pokemon>> getPokemonByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(pokemonService.getPokemonByName(name));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EntityModel<Pokemon>> updatePokemon(@PathVariable("id") String id, @RequestBody Pokemon pokemon) {
        EntityModel<Pokemon> entityModel = pokemonService.updatePokemon(id, pokemon);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> deletePokemon(@PathVariable("id") String id) {
        pokemonService.deletePokemon(id);
        return ResponseEntity.noContent().build();
    }
}
