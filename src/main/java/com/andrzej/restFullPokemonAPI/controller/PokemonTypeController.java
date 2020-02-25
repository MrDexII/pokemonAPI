package com.andrzej.restFullPokemonAPI.controller;

import com.andrzej.restFullPokemonAPI.model.PokemonType;
import com.andrzej.restFullPokemonAPI.service.PokemonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pokemon/type")
public class PokemonTypeController {

    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonTypeController(PokemonTypeService pokemonTypeService) {
        this.pokemonTypeService = pokemonTypeService;
    }

    @PostMapping("/")
    public void createPokemonType(@RequestBody PokemonType pokemonType) {
        pokemonTypeService.createPokemonType(pokemonType);
    }

    @GetMapping("/")
    public List<PokemonType> getAllPokemonTypes() {
        return pokemonTypeService.getAllPokemonTypes();
    }

    @GetMapping("/{id}")
    public PokemonType getPokemonType(@PathVariable("id") String id) {
        return pokemonTypeService.getPokemonType(id);
    }

    @PutMapping("/{id}")
    public void updatePokemonType(@PathVariable("id") String id, @RequestBody PokemonType pokemonType) {
        pokemonTypeService.updatePokemonType(id, pokemonType);
    }

    @DeleteMapping("/{id}")
    public void deletePokemonType(@PathVariable("id") String id) {
        pokemonTypeService.deletePokemonType(id);
    }

}
