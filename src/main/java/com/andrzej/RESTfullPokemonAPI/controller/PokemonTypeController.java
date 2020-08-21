package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.service.PokemonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pokemon/type")
public class PokemonTypeController {

    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonTypeController(PokemonTypeService pokemonTypeService) {
        this.pokemonTypeService = pokemonTypeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPokemonType(@RequestBody PokemonType pokemonType) {
        return pokemonTypeService.createPokemonType(pokemonType);
    }

    @GetMapping("/")
    public ResponseEntity<CollectionModel<EntityModel<PokemonType>>> getAllPokemonTypes() {
        return pokemonTypeService.getAllPokemonTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPokemonType(@PathVariable("id") String id) {
        return pokemonTypeService.getPokemonType(id);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getPokemonTypeByName(@RequestParam("name") String pokemonTypeName) {
        return pokemonTypeService.getPokemonTypeByName(pokemonTypeName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePokemonType(@PathVariable("id") String id, @RequestBody PokemonType pokemonType) {
        return pokemonTypeService.updatePokemonType(id, pokemonType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePokemonType(@PathVariable("id") String id) {
        return pokemonTypeService.deletePokemonType(id);
    }

}
