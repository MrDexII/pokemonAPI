package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.exceptions.PokemonTypeNotFoundException;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.assembler.PokemonTypeModelAssembler;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class PokemonTypeService {
    private PokemonTypeRepository pokemonTypeRepository;
    private PokemonTypeModelAssembler pokemonTypeModelAssembler;

    @Autowired
    public PokemonTypeService(PokemonTypeRepository pokemonTypeRepository,
                              PokemonTypeModelAssembler pokemonTypeModelAssembler) {
        this.pokemonTypeRepository = pokemonTypeRepository;
        this.pokemonTypeModelAssembler = pokemonTypeModelAssembler;
    }

    public EntityModel<PokemonType> createPokemonType(PokemonType pokemonType) {
        return pokemonTypeModelAssembler.toModel(pokemonTypeRepository.save(pokemonType));
    }

    public EntityModel<PokemonType> getPokemonType(String id) {
        return pokemonTypeModelAssembler.toModel(pokemonTypeRepository.findById(id).orElseThrow(
                () -> new PokemonTypeNotFoundException("Pokemon type with id: " + id + " not found")));
    }

    public CollectionModel<EntityModel<PokemonType>> getAllPokemonTypes() {
        return pokemonTypeModelAssembler.toCollectionModel(pokemonTypeRepository.findAll());
    }

    public EntityModel<PokemonType> getPokemonTypeByName(String pokemonTypeName) {
        return pokemonTypeModelAssembler.toModel(pokemonTypeRepository.findByName(pokemonTypeName.toUpperCase()).orElseThrow(
                ()-> new PokemonTypeNotFoundException("Pokemon type with name: " + pokemonTypeName + " not found")));
    }

    public EntityModel<PokemonType> updatePokemonType(String id, PokemonType pokemonType) {
        PokemonType tempPokemonType = pokemonTypeRepository.findById(id).orElseThrow(
                () -> new PokemonTypeNotFoundException("Pokemon type with id: " + id + " not found"));
        pokemonType.setId(tempPokemonType.getId());
        return pokemonTypeModelAssembler.toModel(pokemonTypeRepository.save(pokemonType));
    }

    public void deletePokemonType(String id) {
        pokemonTypeRepository.delete(pokemonTypeRepository.findById(id).orElseThrow(
                () -> new PokemonTypeNotFoundException("Pokemon type with id: " + id + " not found")));
    }

    public boolean isPokemonTypePresent(String pokemonName) {
        return pokemonTypeRepository.findByName(pokemonName).isPresent();
    }
}
