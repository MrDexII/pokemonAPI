package com.andrzej.RESTfullPokemonAPI.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;
import java.util.Objects;

@Document("pokemon")
@TypeAlias("pokemon")
@Relation(collectionRelation = "pokemons", itemRelation = "pokemon")
public class Pokemon {
    @Id
    private ObjectId id;
    private String pokemonName;
    private String imageUrl;
    @DBRef
    private List<PokemonType> pokemonType;

    public Pokemon() {
    }


    @PersistenceConstructor
    public Pokemon(ObjectId id, String pokemonName, String imageUrl, List<PokemonType> pokemonType) {
        this.id = id;
        this.pokemonName = pokemonName;
        this.imageUrl = imageUrl;
        this.pokemonType = pokemonType;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public List<PokemonType> getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(List<PokemonType> pokemonType) {
        this.pokemonType = pokemonType;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id='" + id + '\'' +
                ", name='" + pokemonName + '\'' +
                ", type=" + pokemonType +
                '}';
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Objects.equals(id, pokemon.id) &&
                Objects.equals(pokemonName, pokemon.pokemonName) &&
                Objects.equals(imageUrl, pokemon.imageUrl) &&
                Objects.equals(pokemonType, pokemon.pokemonType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pokemonName, imageUrl, pokemonType);
    }
}
