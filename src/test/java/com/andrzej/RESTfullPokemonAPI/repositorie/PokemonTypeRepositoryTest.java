package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataMongoTest
class PokemonTypeRepositoryTest {

    @Autowired
    private PokemonTypeRepository pokemonTypeRepository;

    static private List<PokemonType> pokemonTypes;

    @BeforeAll
    public static void setup() {
        pokemonTypes = new ArrayList<>();

        PokemonType pokemonTypeFire = new PokemonType();
        pokemonTypeFire.setName("Fire");

        PokemonType pokemonTypeWater = new PokemonType();
        pokemonTypeWater.setName("Water");

        pokemonTypes.add(pokemonTypeFire);
        pokemonTypes.add(pokemonTypeWater);

    }

    @Test
    void NamesInSavedPokemonTypeAndConcretePokemonTypeShouldBeEqualFindByName() {
        pokemonTypeRepository.saveAll(pokemonTypes);

        Optional<PokemonType> optionalFire = pokemonTypeRepository.findByName(pokemonTypes.get(0).getName());
        PokemonType pokemonType = optionalFire.orElseGet(PokemonType::new);

        assertThat(pokemonType.getName(), equalTo(pokemonTypes.get(0).getName()));
        assertThat(pokemonType.get_id(), equalTo(pokemonTypes.get(0).get_id()));

    }
}