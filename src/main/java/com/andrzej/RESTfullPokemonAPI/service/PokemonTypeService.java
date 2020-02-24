package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.exceptions.PokemonTypeNotFoundException;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonTypeService {
    private PokemonTypeRepository pokemonTypeRepository;

    @Autowired
    public PokemonTypeService(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    public void createPokemonType(PokemonType pokemonType) {
        pokemonTypeRepository.save(pokemonType);
    }

    public void updatePokemonType(String id, PokemonType pokemonType) {
        PokemonType tempPokemonType = pokemonTypeRepository.findById(id).orElseThrow(() -> new PokemonTypeNotFoundException("Pokemon type with id: " + id + " not found"));
        pokemonType.setId(tempPokemonType.getId());
        pokemonTypeRepository.save(pokemonType);
    }

    public PokemonType getPokemonType(String id) {
        return pokemonTypeRepository.findById(id).orElseThrow(() -> new PokemonTypeNotFoundException("Pokemon type with id: " + id + " not found"));
    }

    public List<PokemonType> getAllPokemonTypes() {
        return pokemonTypeRepository.findAll();
    }

    public void deletePokemonType(String id) {
        pokemonTypeRepository.delete(pokemonTypeRepository.findById(id).orElseThrow(() -> new PokemonTypeNotFoundException("Pokemon type with id: " + id + " not found")));
    }

}
