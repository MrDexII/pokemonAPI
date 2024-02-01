package com.andrzej.RESTfullPokemonAPI.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Objects;

@Document(indexName = "pokemon_db.pokemon")
public class Pokemon {
    @Id
    private ObjectId _id;
    private Integer number;
    private String name;
    private String fotoUrl;
    private List<String> types;
    private PokemonStats pokemonStats;
    @Transient
    private boolean isPokemonDead;

    public Pokemon() {
    }

    public Pokemon(Integer number, String name, String fotoUrl, List<String> types, PokemonStats pokemonStats) {
        this.number = number;
        this.name = name;
        this.fotoUrl = fotoUrl;
        this.types = types;
        this.pokemonStats = pokemonStats;
        this.isPokemonDead = false;
    }

    @PersistenceConstructor
    public Pokemon(ObjectId _id, Integer number, String name, String fotoUrl, List<String> types, PokemonStats pokemonStats) {
        this._id = _id;
        this.number = number;
        this.name = name;
        this.fotoUrl = fotoUrl;
        this.types = types;
        this.pokemonStats = pokemonStats;
    }

    public String get_id() {
        return this._id.toString();
    }

    public void set_id(String _id) {
        this._id = new ObjectId(_id);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public PokemonStats getPokemonStats() {
        return pokemonStats;
    }

    public void setPokemonStats(PokemonStats pokemonStats) {
        this.pokemonStats = pokemonStats;
    }

    public boolean isPokemonDead() {
        return isPokemonDead;
    }

    public void setPokemonDead(boolean pokemonDead) {
        isPokemonDead = pokemonDead;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "_id=" + _id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", fotoUrl='" + fotoUrl + '\'' +
                ", types=" + types +
                ", pokemonStats=" + pokemonStats +
                ", isPokemonDead=" + isPokemonDead +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return isPokemonDead == pokemon.isPokemonDead && Objects.equals(_id, pokemon._id) && Objects.equals(number, pokemon.number) && Objects.equals(name, pokemon.name) && Objects.equals(fotoUrl, pokemon.fotoUrl) && Objects.equals(types, pokemon.types) && Objects.equals(pokemonStats, pokemon.pokemonStats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, number, name, fotoUrl, types, pokemonStats, isPokemonDead);
    }
}
