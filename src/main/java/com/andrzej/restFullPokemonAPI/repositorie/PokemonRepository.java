package com.andrzej.restFullPokemonAPI.repositorie;

import com.andrzej.restFullPokemonAPI.model.Pokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, String> {
    Pokemon findByPokemonName(String name);
}
