package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonTypeModelAssembler;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonTypeService {

    private final PokemonTypeRepository pokemonTypeRepository;
    private final PokemonTypeModelAssembler pokemonTypeModelAssembler;

    @Autowired
    public PokemonTypeService(PokemonTypeRepository pokemonTypeRepository,
                              PokemonTypeModelAssembler pokemonTypeModelAssembler) {
        this.pokemonTypeRepository = pokemonTypeRepository;
        this.pokemonTypeModelAssembler = pokemonTypeModelAssembler;
    }

    public ResponseEntity<?> createPokemonType(PokemonType newPokemonType) {
        String typeName = newPokemonType.getName();
        Optional<PokemonType> pokemonTypeOptional = pokemonTypeRepository.findByName(typeName);

        if (pokemonTypeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pokemon type with name: " + typeName + " already exists");
        }

        EntityModel<PokemonType> pokemonTypeEntityModel = pokemonTypeModelAssembler.toModel(pokemonTypeRepository.save(newPokemonType));
        return ResponseEntity.created(pokemonTypeEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(pokemonTypeEntityModel);
    }

    public ResponseEntity<?> getPokemonType(String id) {
        Optional<PokemonType> pokemonType = pokemonTypeRepository.findById(id);
        return pokemonType.isPresent() ?
                ResponseEntity.ok(pokemonTypeModelAssembler.toModel(pokemonType.get())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon type with id: " + id + " not exists");
    }

    public ResponseEntity<CollectionModel<EntityModel<PokemonType>>> getAllPokemonTypes() {
        List<PokemonType> allPokemonTypes = pokemonTypeRepository.findAll();
        if (allPokemonTypes == null || allPokemonTypes.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<EntityModel<PokemonType>> allPokemonTypesModel = pokemonTypeModelAssembler.toCollectionModel(allPokemonTypes);
        return ResponseEntity.ok(allPokemonTypesModel);
    }

    public ResponseEntity<?> getPokemonTypeByName(String pokemonTypeName) {
        Optional<PokemonType> pokemonTypeNameOptional = pokemonTypeRepository.findByName(pokemonTypeName);
        return pokemonTypeNameOptional.isPresent() ?
                ResponseEntity.ok(pokemonTypeModelAssembler.toModel(pokemonTypeNameOptional.get())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon type with name: " + pokemonTypeName + " not exists");
    }

    public ResponseEntity<?> updatePokemonType(String id, PokemonType pokemonType) {
        if (!pokemonTypeRepository.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon type with id: " + id + " not exists");
        }
        pokemonType.set_id(id);
        return ResponseEntity.ok(pokemonTypeModelAssembler.toModel(pokemonTypeRepository.save(pokemonType)));
    }

    public ResponseEntity<?> deletePokemonType(String id) {
        Optional<PokemonType> pokemonType = pokemonTypeRepository.findById(id);
        if (pokemonType.isPresent()) {
            pokemonTypeRepository.delete(pokemonType.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon type with id: " + id + " not exists");
        }
    }
}
