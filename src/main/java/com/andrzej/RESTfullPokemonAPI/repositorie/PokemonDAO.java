package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PokemonDAO implements PokemonRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PokemonDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Pokemon> findByPokemonName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("pokemonName").is(name));
        return Optional.ofNullable(mongoTemplate.findOne(query, Pokemon.class));
    }

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Pokemon> pokemons = mongoTemplate.find(query, Pokemon.class);
        return PageableExecutionUtils.getPage(
                pokemons,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Pokemon.class));
    }

    @Override
    public Pokemon save(Pokemon pokemon) {
        return mongoTemplate.save(pokemon);
    }

    @Override
    public Optional<Pokemon> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Pokemon.class));
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Pokemon> findAll() {
        return null;
    }

    @Override
    public void delete(Pokemon pokemon) {
        mongoTemplate.remove(pokemon);
    }
}
