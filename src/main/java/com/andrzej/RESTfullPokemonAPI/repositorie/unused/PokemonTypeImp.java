package com.andrzej.RESTfullPokemonAPI.repositorie.unused;

import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

//@Repository
public class PokemonTypeImp {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PokemonTypeImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<PokemonType> findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name.toUpperCase()));
        return Optional.ofNullable(mongoTemplate.findOne(query, PokemonType.class));
    }

    public PokemonType save(PokemonType type) {
        return mongoTemplate.save(type);
    }

    public Optional<PokemonType> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, PokemonType.class));
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    public List<PokemonType> findAll() {
        Query query = new Query();
        return mongoTemplate.find(query, PokemonType.class);
    }

    public void delete(PokemonType type) {
        mongoTemplate.remove(type);
    }
}
