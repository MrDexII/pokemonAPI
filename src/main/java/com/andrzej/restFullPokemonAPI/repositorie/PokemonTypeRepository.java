package com.andrzej.restFullPokemonAPI.repositorie;

import com.andrzej.restFullPokemonAPI.model.PokemonType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonTypeRepository extends MongoRepository<PokemonType, String> {
}
