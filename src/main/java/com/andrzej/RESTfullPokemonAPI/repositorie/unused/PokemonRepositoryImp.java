package com.andrzej.RESTfullPokemonAPI.repositorie.unused;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

//@Repository
public class PokemonRepositoryImp {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PokemonRepositoryImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Pokemon> findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return Optional.ofNullable(mongoTemplate.findOne(query, Pokemon.class));
    }

    public Optional<Pokemon> findByNumber(Integer number) {
        Query query = new Query();
        query.addCriteria(Criteria.where("number").is(number));
        return Optional.ofNullable(mongoTemplate.findOne(query, Pokemon.class));
    }

    public Page<Pokemon> findAll(Pageable pageable) {
        Query query = new Query()
                .with(pageable)
                .with(Sort.by(Sort.Direction.ASC, "number"));
        List<Pokemon> pokemons = mongoTemplate.find(query, Pokemon.class);
        return PageableExecutionUtils.getPage(
                pokemons,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Pokemon.class));
    }

    public Long countPokemon() {
        Query query = new Query();
        return mongoTemplate.count(query, Pokemon.class);
    }

    public Pokemon save(Pokemon pokemon) {
        return mongoTemplate.save(pokemon);
    }

    public Optional<Pokemon> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Pokemon.class));
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    public List<Pokemon> findAll() {
        return null;
    }

    public void delete(Pokemon pokemon) {
        mongoTemplate.remove(pokemon);
    }
}
