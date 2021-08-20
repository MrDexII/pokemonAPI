package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.model.PokemonType;

import java.util.Optional;

public interface PokemonTypeRepository extends DAORepository<PokemonType, String> {
    Optional<PokemonType> findByName(String name);
}
