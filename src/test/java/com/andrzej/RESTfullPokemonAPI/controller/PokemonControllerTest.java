package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.RoleRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import com.andrzej.RESTfullPokemonAPI.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class PokemonControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonRepository pokemonRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ApplicationUserService applicationUserService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private SecretKey secretKey;

    @MockBean
    private JwtConfig jwtConfig;

    @SpyBean
    private PokemonModelAssembler pokemonModelAssembler;

    @SpyBean
    private PokemonService pokemonService;

    @SpyBean
    PagedResourcesAssembler<Pokemon> pagedResourcesAssembler;

    @Test
    void ShouldReturnStatus201AndCreatePokemon() throws Exception {
        List<PokemonType> pokemonTypes = List.of(new PokemonType(new ObjectId(), "Fire"));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                pokemonTypes);

        EntityModel<Pokemon> pokemonEntityModel = pokemonModelAssembler.toModel(pokemon);
        String expectedURL = "http://localhost/pokemon/" + pokemon.getId();

        given(this.pokemonRepository.findByPokemonName(pokemon.getPokemonName())).willReturn(Optional.empty());
        given(this.pokemonRepository.save(pokemon)).willReturn(pokemon);

        this.mockMvc.perform(
                post("/pokemon/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonEntityModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(pokemon.getId())))
                .andExpect(jsonPath("$.pokemonName", is("charizard")))
                .andExpect(jsonPath("$.imageUrl", is("URL")))
                .andExpect(jsonPath("$.pokemonType", hasSize(1)))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("id", pokemon.getPokemonType().get(0).getId())))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("name", "Fire")))
                .andExpect(jsonPath("$._links.self.href", is(expectedURL)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedURL)));
    }

    @Test
    void ShouldReturnStatus409CreatePokemon() throws Exception {
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                List.of(new PokemonType(new ObjectId(), "Fire")));

        EntityModel<Pokemon> pokemonEntityModel = pokemonModelAssembler.toModel(pokemon);

        given(this.pokemonRepository.findByPokemonName(pokemon.getPokemonName())).willReturn(Optional.of(pokemon));

        this.mockMvc.perform(
                post("/pokemon/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonEntityModel)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Pokemon with name: " + pokemon.getPokemonName() + " already exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnListOfPokemonsGetAllPokemons() throws Exception {
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "charizardURLImage",
                List.of(
                        new PokemonType(new ObjectId(), "FIRE"),
                        new PokemonType(new ObjectId(), "FLYING")
                ));

        Pokemon pokemon2 = new Pokemon(
                new ObjectId(),
                "blastoise",
                "blastoiseURLImage",
                List.of(new PokemonType(new ObjectId(), "WATER")));

        List<Pokemon> pokemons = List.of(pokemon, pokemon2);
        String expectedCharizardLink = "http://localhost/pokemon/" + pokemon.getId();
        String expectedBlastoiseLink = "http://localhost/pokemon/" + pokemon2.getId();

        Pageable pageable = PageRequest.of(0, 2);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Page<Pokemon> pages = new PageImpl<>(
                pokemons.subList(pageNumber * pageSize, pageNumber * pageSize + pageSize),
                pageable,
                pokemons.size());

        given(this.pokemonRepository.findAll(pageable)).willReturn(pages);

        this.mockMvc.perform(
                get("/pokemon/all")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pokemons", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemons[0].id", is(pokemons.get(0).getId())))
                .andExpect(jsonPath("$._embedded.pokemons[0].pokemonName", is("charizard")))
                .andExpect(jsonPath("$._embedded.pokemons[0].imageUrl", is("charizardURLImage")))
                .andExpect(jsonPath("$._embedded.pokemons[0].pokemonType", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemons[0].pokemonType[0]",
                        hasEntry("id", pokemons.get(0).getPokemonType().get(0).getId())))
                .andExpect(jsonPath("$._embedded.pokemons[0].pokemonType[0]", hasEntry("name", "FIRE")))
                .andExpect(jsonPath("$._embedded.pokemons[0].pokemonType[1]",
                        hasEntry("id", pokemons.get(0).getPokemonType().get(1).getId())))
                .andExpect(jsonPath("$._embedded.pokemons[0].pokemonType[1]", hasEntry("name", "FLYING")))
                .andExpect(jsonPath("$._embedded.pokemons[0]._links.self.href", is(expectedCharizardLink)))
                .andExpect(jsonPath("$._embedded.pokemons[0]._links.delete.href", is(expectedCharizardLink)))
                .andExpect(jsonPath("$._embedded.pokemons[1].id", is(pokemons.get(1).getId())))
                .andExpect(jsonPath("$._embedded.pokemons[1].pokemonName", is("blastoise")))
                .andExpect(jsonPath("$._embedded.pokemons[1].imageUrl", is("blastoiseURLImage")))
                .andExpect(jsonPath("$._embedded.pokemons[1].pokemonType", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pokemons[1].pokemonType[0]",
                        hasEntry("id", pokemons.get(1).getPokemonType().get(0).getId())))
                .andExpect(jsonPath("$._embedded.pokemons[1].pokemonType[0]", hasEntry("name", "WATER")))
                .andExpect(jsonPath("$._embedded.pokemons[1]._links.self.href", is(expectedBlastoiseLink)))
                .andExpect(jsonPath("$._embedded.pokemons[1]._links.delete.href", is(expectedBlastoiseLink)))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/pokemon/all?page=" +
                                pageable.getPageNumber() + "&size=" +
                                pageable.getPageSize() + "")))
                .andExpect(jsonPath("$.page.size", is(pageable.getPageSize())))
                .andExpect(jsonPath("$.page.totalElements", is(pokemons.size())))
                .andExpect(jsonPath("$.page.totalPages", is(pokemons.size() / pageable.getPageSize())))
                .andExpect(jsonPath("$.page.number", is(pageable.getPageNumber())));
    }

    @Test
    void ShouldReturnStatus200AndReturnEmptyListGetAllPokemons() throws Exception {
        List<Pokemon> pokemons = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 2);
        Page<Pokemon> pages = new PageImpl<>(pokemons, pageable, pokemons.size());
        given(this.pokemonRepository.findAll(pageable)).willReturn(pages);

        this.mockMvc.perform(
                get("/pokemon/all")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatusOKWhenIdIsCorrectGetPokemonById() throws Exception {
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                List.of(new PokemonType(new ObjectId(), "Fire")));

        String expectedPokemonSelfDeleteLink = "http://localhost/pokemon/" + pokemon.getId();

        given(this.pokemonRepository.findById(pokemon.getId())).willReturn(Optional.of(pokemon));


        this.mockMvc.perform(
                get("/pokemon/" + pokemon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemon.getId())))
                .andExpect(jsonPath("$.pokemonName", is("charizard")))
                .andExpect(jsonPath("$.imageUrl", is("URL")))
                .andExpect(jsonPath("$.pokemonType", hasSize(1)))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("id", pokemon.getPokemonType().get(0).getId())))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("name", "Fire")))
                .andExpect(jsonPath("$._links.self.href", is(expectedPokemonSelfDeleteLink)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedPokemonSelfDeleteLink)));
    }

    @Test
    void ShouldReturnStatus404WhenIdIsNotCorrectGetPokemonById() throws Exception {
        String falseId = new ObjectId().toString();

        given(this.pokemonRepository.findById(falseId)).willReturn(Optional.empty());

        this.mockMvc.perform(
                get("/pokemon/" + falseId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with id: " + falseId + " not exists"));

    }

    @Test
    void ShouldReturnStatus200WhenNameIsCorrectGetPokemonByName() throws Exception {
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                List.of(new PokemonType(new ObjectId(), "Fire")));

        String expectedPokemonSelfDeleteLink = "http://localhost/pokemon/" + pokemon.getId();

        given(this.pokemonRepository.findByPokemonName(anyString())).willReturn(Optional.of(pokemon));

        this.mockMvc.perform(
                get("/pokemon/find")
                        .param("name", "charizard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemon.getId())))
                .andExpect(jsonPath("$.pokemonName", is("charizard")))
                .andExpect(jsonPath("$.imageUrl", is("URL")))
                .andExpect(jsonPath("$.pokemonType", hasSize(1)))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("id", pokemon.getPokemonType().get(0).getId())))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("name", "Fire")))
                .andExpect(jsonPath("$._links.self.href", is(expectedPokemonSelfDeleteLink)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedPokemonSelfDeleteLink)));
    }

    @Test
    void ShouldReturnStatus404WhenNameIsNotCorrectGetPokemonByName() throws Exception {
        String notValidPokemonName = "asdasd";

        given(this.pokemonRepository.findByPokemonName(anyString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                get("/pokemon/find")
                        .param("name", notValidPokemonName))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with name: " + notValidPokemonName + " not exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnSavedPokemonUpdatePokemon() throws Exception {
        Pokemon pokemonBeforeUpdate = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                List.of(new PokemonType(new ObjectId(), "Fire")));

        Pokemon pokemonAfterUpdate = new Pokemon(
                new ObjectId(),
                "charizardUpdated",
                "URLUpdated",
                List.of(new PokemonType(new ObjectId(), "Fire"),
                        new PokemonType(new ObjectId(), "Flying")));

        String expectedPokemonSelfDeleteLink = "http://localhost/pokemon/" + pokemonAfterUpdate.getId();

        given(this.pokemonRepository.findById(pokemonAfterUpdate.getId())).willReturn(Optional.of(pokemonBeforeUpdate));
        given(this.pokemonRepository.save(pokemonAfterUpdate)).willReturn(pokemonAfterUpdate);

        this.mockMvc.perform(
                put("/pokemon/" + pokemonAfterUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonAfterUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemonAfterUpdate.getId())))
                .andExpect(jsonPath("$.pokemonName", is("charizardUpdated")))
                .andExpect(jsonPath("$.imageUrl", is("URLUpdated")))
                .andExpect(jsonPath("$.pokemonType", hasSize(2)))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("id", pokemonAfterUpdate.getPokemonType().get(0).getId())))
                .andExpect(jsonPath("$.pokemonType[0]", hasEntry("name", "Fire")))
                .andExpect(jsonPath("$.pokemonType[1]", hasEntry("id", pokemonAfterUpdate.getPokemonType().get(1).getId())))
                .andExpect(jsonPath("$.pokemonType[1]", hasEntry("name", "Flying")))
                .andExpect(jsonPath("$._links.self.href", is(expectedPokemonSelfDeleteLink)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedPokemonSelfDeleteLink)));
    }

    @Test
    void ShouldReturnStatus404UpdatePokemon() throws Exception {
        Pokemon pokemonAfterUpdate = new Pokemon(
                new ObjectId(),
                "charizardUpdated",
                "URLUpdated",
                List.of(new PokemonType(new ObjectId(), "Fire"),
                        new PokemonType(new ObjectId(), "Flying")));

        String falseId = new ObjectId().toString();

        given(this.pokemonRepository.findById(anyString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                put("/pokemon/" + falseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonAfterUpdate)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with id: " + falseId + " not exists"));

    }

    @Test
    void ShouldReturnStatus204DeletePokemon() throws Exception {
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                List.of(new PokemonType(new ObjectId(), "Fire")));

        given(this.pokemonRepository.findById(anyString())).willReturn(Optional.of(pokemon));

        this.mockMvc.perform(
                delete("/pokemon/" + pokemon.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatus404DeletePokemon() throws Exception {
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                List.of(new PokemonType(new ObjectId(), "Fire")));

        given(this.pokemonRepository.findById(anyString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                delete("/pokemon/" + pokemon.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with id: " + pokemon.getId() + " not exists"));
    }
}