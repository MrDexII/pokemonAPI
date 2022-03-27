package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonStats;
import com.andrzej.RESTfullPokemonAPI.model.Stats;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private PokemonModelAssembler pokemonModelAssembler;

    @Mock
    private PagedResourcesAssembler<Pokemon> pagedResourcesAssembler;

    @InjectMocks
    private PokemonService pokemonService;
    private Pokemon pokemon;

    @Before
    public void setup() {
        String fireType = "Fire";
        String flyingType = "Flying";
        List<String> pokemonTypes = new ArrayList<>();
        pokemonTypes.add(fireType);
        pokemonTypes.add(flyingType);
        PokemonStats pokemonStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        this.pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                pokemonTypes,
                pokemonStats);
    }

    @Test
    public void ShouldReturnStatus201createPokemon() {
        given(pokemonRepository.findByName(any(String.class))).willReturn(Optional.empty());
        given(pokemonModelAssembler.toModel(any(Pokemon.class))).willReturn(EntityModel.of(this.pokemon, Link.of("test_link")));
        given(pokemonRepository.save(any(Pokemon.class))).willReturn(this.pokemon);

        ResponseEntity<?> responseEntity = pokemonService.createPokemon(this.pokemon);

        assertThat(responseEntity.getStatusCodeValue(), equalTo(201));
        assertThat(responseEntity.getBody().toString(), containsString(this.pokemon.get_id()));
    }

    @Test
    public void ShouldReturnStatus409createPokemon() {
        given(pokemonRepository.findByName(any(String.class))).willReturn(Optional.of(this.pokemon));

        ResponseEntity<?> responseEntity = pokemonService.createPokemon(this.pokemon);

        assertThat(responseEntity.getStatusCodeValue(), equalTo(409));
        assertThat(responseEntity.getBody(), equalTo("Pokemon with name: " + this.pokemon.getName() + " already exists"));
    }
}