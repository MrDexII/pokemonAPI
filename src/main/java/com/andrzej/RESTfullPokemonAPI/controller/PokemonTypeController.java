package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.service.PokemonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pokemon/type")
public class PokemonTypeController {

    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonTypeController(PokemonTypeService pokemonTypeService) {
        this.pokemonTypeService = pokemonTypeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPokemonType(@RequestBody PokemonType pokemonType, UriComponentsBuilder uriComponentsBuilder) {
        String typeName = pokemonType.getName();

        if (pokemonTypeService.isPokemonTypePresent(typeName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pokemon type with name: " + typeName + " already exists");
        }

        EntityModel<PokemonType> pokemonTypeEntityModel = pokemonTypeService.createPokemonType(pokemonType);
        return ResponseEntity.created(pokemonTypeEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(pokemonTypeEntityModel);
    }

    @GetMapping("/")
    public ResponseEntity<CollectionModel<EntityModel<PokemonType>>> getAllPokemonTypes() {
        CollectionModel<EntityModel<PokemonType>> allPokemonTypes = pokemonTypeService.getAllPokemonTypes();
        return ResponseEntity.ok(allPokemonTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PokemonType>> getPokemonType(@PathVariable("id") String id) {
        EntityModel<PokemonType> pokemonType = pokemonTypeService.getPokemonType(id);
        return ResponseEntity.ok(pokemonType);
    }

    @GetMapping("/find")
    public ResponseEntity<EntityModel<PokemonType>> getPokemonTypeByName(@RequestParam("name") String pokemonTypeName) {
        EntityModel<PokemonType> pokemonType = pokemonTypeService.getPokemonTypeByName(pokemonTypeName);
        return ResponseEntity.ok(pokemonType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PokemonType>> updatePokemonType(@PathVariable("id") String id, @RequestBody PokemonType pokemonType) {
        EntityModel<PokemonType> pokemonTypeEntityModel = pokemonTypeService.updatePokemonType(id, pokemonType);
        return ResponseEntity.created(pokemonTypeEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(pokemonTypeEntityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePokemonType(@PathVariable("id") String id) {
        pokemonTypeService.deletePokemonType(id);
        return ResponseEntity.noContent().build();
    }

}
