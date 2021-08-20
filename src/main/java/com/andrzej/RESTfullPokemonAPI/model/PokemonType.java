package com.andrzej.RESTfullPokemonAPI.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("type")
public class PokemonType {
    @Id
    private ObjectId _id;
    private String name;

    public PokemonType() {
    }

    public PokemonType(String name) {
        this.name = name;
    }

    @PersistenceConstructor
    public PokemonType(ObjectId _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String get_id() {
        return this._id.toString();
    }

    public void set_id(String _id) {
        this._id = new ObjectId(_id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PokemonType{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonType that = (PokemonType) o;
        return Objects.equals(_id, that._id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, name);
    }
}

/*enum PokemonType{
    NORMAL,
    FIRE,
    WATER,
    ELECTRIC,
    GRASS,
    ICE,
    FIGHTING,
    POISON,
    GROUND,
    FLYING,
    PSYCHIC,
    BUG,
    ROCK,
    GHOST,
    DRAGON,
    DARK,
    STEEL,
    FAIRY
}*/