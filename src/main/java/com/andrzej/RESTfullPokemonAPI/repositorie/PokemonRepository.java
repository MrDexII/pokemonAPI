package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, String> {
    Optional<Pokemon> findByPokemonName(String name);
}
