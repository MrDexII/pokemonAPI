package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.elasticsearch.repository.MyElasticsearchRepository;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonStats;
import com.andrzej.RESTfullPokemonAPI.model.Stats;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataMongoTest
class PokemonRepositoryTest {

    @Autowired
    private PokemonRepository pokemonRepository;

    @MockBean
    private MyElasticsearchRepository myElasticsearchRepository;

    private static List<Pokemon> pokemons;

    @BeforeAll
    public static void setup() {
        pokemons = new ArrayList<>();
        Pokemon pokemon1;

        String charizardType1 = "Fire";
        String charizardType2 = "Flying";
        List<String> pokemonTypes = new ArrayList<>();
        pokemonTypes.add(charizardType1);
        pokemonTypes.add(charizardType2);
        PokemonStats pokemonStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        pokemon1 = new Pokemon(
                null,
                1,
                "charizard",
                "testURL",
                pokemonTypes,
                pokemonStats);

        Pokemon pokemon2;
        String venusaurType1 = "grass";
        String venusaurType2 = "poison";
        List<String> venusaurTypes = new ArrayList<>();
        venusaurTypes.add(venusaurType1);
        venusaurTypes.add(venusaurType2);
        PokemonStats venusaurStats = new PokemonStats(
                new Stats(80, 270, 364),
                new Stats(82, 152, 289),
                new Stats(83, 153, 291),
                new Stats(100, 184, 328),
                new Stats(100, 184, 328),
                new Stats(80, 148, 284));
        pokemon2 = new Pokemon(
                null,
                2,
                "venusaur",
                "testURL",
                venusaurTypes,
                venusaurStats
        );
        pokemons.add(pokemon1);
        pokemons.add(pokemon2);
    }

    @Test
    void pokemonSavedInDbShouldBeEqualToCreatedPokemonFindByName() {
        pokemonRepository.save(pokemons.get(0));
        Optional<Pokemon> byName = pokemonRepository.findByName(pokemons.get(0).getName());
        Pokemon pokemon = byName.orElseGet(Pokemon::new);

        assertThat(pokemon.get_id(), equalTo(pokemons.get(0).get_id()));
        assertThat(pokemon.getName(), equalTo(pokemons.get(0).getName()));
        assertThat(pokemon.getNumber(), equalTo(pokemons.get(0).getNumber()));
    }

    @Test
    void ShouldReturnPokemonOfTheSameNameFindByNumber() {
        pokemonRepository.save(pokemons.get(0));
        Optional<Pokemon> byName = pokemonRepository.findByNumber(pokemons.get(0).getNumber());
        Pokemon pokemon = byName.orElseGet(Pokemon::new);

        assertThat(pokemon.get_id(), equalTo(pokemons.get(0).get_id()));
        assertThat(pokemon.getName(), equalTo(pokemons.get(0).getName()));
        assertThat(pokemon.getNumber(), equalTo(pokemons.get(0).getNumber()));
    }

    @Test
    void ShouldReturnTwoPokemonFindAll() {
        pokemonRepository.saveAll(pokemons);
        Page<Pokemon> all = pokemonRepository.findAll(Pageable.ofSize(1));

        assertThat(all.getTotalElements(), equalTo(2L));
    }

    @Test
    void ShouldReturnTwoSavingTwoEntitiesCountByName() {
        pokemonRepository.saveAll(pokemons);
        Long pokemonNumbers = pokemonRepository.count();

        assertThat(pokemonNumbers, equalTo(2L));
    }
}