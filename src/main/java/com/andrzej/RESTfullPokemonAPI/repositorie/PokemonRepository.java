package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PokemonRepository extends DAORepository<Pokemon, String> {
    Optional<Pokemon> findByPokemonName(String name);

    Page<Pokemon> findAll(Pageable page);
}
