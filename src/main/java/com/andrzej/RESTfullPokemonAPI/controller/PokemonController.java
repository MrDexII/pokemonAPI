package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        return pokemonService.createPokemon(pokemon);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getAllPokemons(@RequestParam(required = false) Optional<Integer> page,
                                            @RequestParam(required = false) Optional<Integer> size) {
        return pokemonService.getAllPokemons(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPokemonById(@PathVariable("id") String id) {
        return pokemonService.getPokemonById(id);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getPokemonByName(@RequestParam("name") String name) {
        return pokemonService.getPokemonByName(name);
    }

    @GetMapping("/findByPokemonNumber")
    public ResponseEntity<?> getPokemonByNumber(@RequestParam("number") Integer number) {
        return pokemonService.getPokemonByNumber(number);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePokemon(@PathVariable("id") String id, @RequestBody Pokemon pokemon) {
        return pokemonService.updatePokemon(id, pokemon);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deletePokemon(@PathVariable("id") String id) {
        return pokemonService.deletePokemon(id);
    }

}
