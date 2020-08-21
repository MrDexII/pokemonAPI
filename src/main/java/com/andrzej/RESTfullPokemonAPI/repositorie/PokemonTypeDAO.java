package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PokemonTypeDAO implements PokemonTypeRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PokemonTypeDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<PokemonType> findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name.toUpperCase()));
        return Optional.ofNullable(mongoTemplate.findOne(query, PokemonType.class));
    }

    @Override
    public PokemonType save(PokemonType type) {
        return mongoTemplate.save(type);
    }

    @Override
    public Optional<PokemonType> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, PokemonType.class));
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public List<PokemonType> findAll() {
        Query query = new Query();
        return mongoTemplate.find(query, PokemonType.class);
    }

    @Override
    public void delete(PokemonType type) {
        mongoTemplate.remove(type);
    }
}
