package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonStats;
import com.andrzej.RESTfullPokemonAPI.model.Stats;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        String fireType = "Fire";
        List<String> pokemonTypes = new ArrayList<>();
        pokemonTypes.add(fireType);
        PokemonStats pokemonStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                pokemonTypes,
                pokemonStats);

        EntityModel<Pokemon> pokemonEntityModel = pokemonModelAssembler.toModel(pokemon);
        String expectedURL = "http://localhost/pokemon/" + pokemon.get_id();

        given(this.pokemonRepository.findByName(pokemon.getName())).willReturn(Optional.empty());
        given(this.pokemonRepository.save(pokemon)).willReturn(pokemon);

        this.mockMvc.perform(
                        post("/pokemon/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(pokemonEntityModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._id", is(pokemon.get_id())))
                .andExpect(jsonPath("$.name", is("charizard")))
                .andExpect(jsonPath("$.fotoUrl", is("URL")))
                .andExpect(jsonPath("$.types", hasSize(1)))
                .andExpect(jsonPath("$.types[0]", is("Fire")))
                .andExpect(jsonPath("$._links.self.href", is(expectedURL)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedURL)));
    }

    @Test
    void ShouldReturnStatus409CreatePokemon() throws Exception {
        String fireType = "Fire";
        List<String> pokemonTypes = new ArrayList<>();
        pokemonTypes.add(fireType);
        PokemonStats pokemonStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                pokemonTypes,
                pokemonStats);

        EntityModel<Pokemon> pokemonEntityModel = pokemonModelAssembler.toModel(pokemon);

        given(this.pokemonRepository.findByName(pokemon.getName())).willReturn(Optional.of(pokemon));

        this.mockMvc.perform(
                        post("/pokemon/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(pokemonEntityModel)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Pokemon with name: " + pokemon.getName() + " already exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnListOfPokemonsGetAllPokemons() throws Exception {
        List<String> charizardTypes = new ArrayList<>();
        charizardTypes.add("Fire");
        charizardTypes.add("Flying");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "charizardURLImage",
                charizardTypes,
                charizardTypesStats);

        List<String> blastoiseTypes = new ArrayList<>();
        blastoiseTypes.add("Water");

        PokemonStats blastoiseStats = new PokemonStats(
                new Stats(79, 268, 362),
                new Stats(83, 153, 291),
                new Stats(100, 184, 328),
                new Stats(85, 157, 295),
                new Stats(105, 193, 339),
                new Stats(78, 144, 280));

        Pokemon pokemon2 = new Pokemon(
                new ObjectId(),
                2,
                "blastoise",
                "blastoiseURLImage",
                blastoiseTypes,
                blastoiseStats);

        List<Pokemon> pokemons = new ArrayList<>();
        pokemons.add(pokemon);
        pokemons.add(pokemon2);

        String expectedCharizardLink = "http://localhost/pokemon/" + pokemon.get_id();
        String expectedBlastoiseLink = "http://localhost/pokemon/" + pokemon2.get_id();

        Pageable pageable = PageRequest.of(0, 2);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Page<Pokemon> pages = new PageImpl<>(
                pokemons.subList(pageNumber * pageSize, pageNumber * pageSize + pageSize),
                pageable,
                pokemons.size());

        given(this.pokemonRepository.findAll(pageable)).willReturn(pages);

        this.mockMvc.perform(
                        get("/pokemon/")
                                .param("page", "0")
                                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pokemonList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemonList[0]._id", is(pokemons.get(0).get_id())))
                .andExpect(jsonPath("$._embedded.pokemonList[0].name", is("charizard")))
                .andExpect(jsonPath("$._embedded.pokemonList[0].fotoUrl", is("charizardURLImage")))
                .andExpect(jsonPath("$._embedded.pokemonList[0].types", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemonList[0].types[0]", is("Fire")))
                .andExpect(jsonPath("$._embedded.pokemonList[0].types[1]", is("Flying")))
                .andExpect(jsonPath("$._embedded.pokemonList[0]._links.self.href", is(expectedCharizardLink)))
                .andExpect(jsonPath("$._embedded.pokemonList[0]._links.delete.href", is(expectedCharizardLink)))
                .andExpect(jsonPath("$._embedded.pokemonList[1]._id", is(pokemons.get(1).get_id())))
                .andExpect(jsonPath("$._embedded.pokemonList[1].name", is("blastoise")))
                .andExpect(jsonPath("$._embedded.pokemonList[1].fotoUrl", is("blastoiseURLImage")))
                .andExpect(jsonPath("$._embedded.pokemonList[1].types", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pokemonList[1].types[0]", is("Water")))
                .andExpect(jsonPath("$._embedded.pokemonList[1]._links.self.href", is(expectedBlastoiseLink)))
                .andExpect(jsonPath("$._embedded.pokemonList[1]._links.delete.href", is(expectedBlastoiseLink)))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/pokemon/?page=" +
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
                        get("/pokemon/")
                                .param("page", "0")
                                .param("size", "2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatusOKWhenIdIsCorrectGetPokemonById() throws Exception {
        List<String> charizardTypes = new ArrayList<>();
        charizardTypes.add("Fire");
        charizardTypes.add("Flying");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                charizardTypes,
                charizardTypesStats);

        String expectedPokemonSelfDeleteLink = "http://localhost/pokemon/" + pokemon.get_id();

        given(this.pokemonRepository.findById(pokemon.get_id().toString())).willReturn(Optional.of(pokemon));


        this.mockMvc.perform(
                        get("/pokemon/" + pokemon.get_id().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", is(pokemon.get_id())))
                .andExpect(jsonPath("$.name", is("charizard")))
                .andExpect(jsonPath("$.fotoUrl", is("URL")))
                .andExpect(jsonPath("$.types", hasSize(2)))
                .andExpect(jsonPath("$.types[0]", is("Fire")))
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
        List<String> charizardTypes = new ArrayList<>();
        charizardTypes.add("Fire");
        charizardTypes.add("Flying");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                charizardTypes,
                charizardTypesStats);

        String expectedPokemonSelfDeleteLink = "http://localhost/pokemon/" + pokemon.get_id();

        given(this.pokemonRepository.findByName(anyString())).willReturn(Optional.of(pokemon));

        this.mockMvc.perform(
                        get("/pokemon/find")
                                .param("name", "charizard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", is(pokemon.get_id())))
                .andExpect(jsonPath("$.name", is("charizard")))
                .andExpect(jsonPath("$.fotoUrl", is("URL")))
                .andExpect(jsonPath("$.types", hasSize(2)))
                .andExpect(jsonPath("$.types[0]", is("Fire")))
                .andExpect(jsonPath("$._links.self.href", is(expectedPokemonSelfDeleteLink)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedPokemonSelfDeleteLink)));
    }

    @Test
    void ShouldReturnStatus404WhenNameIsNotCorrectGetPokemonByName() throws Exception {
        String notValidPokemonName = "asdasd";

        given(this.pokemonRepository.findByName(anyString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                        get("/pokemon/find")
                                .param("name", notValidPokemonName))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with name: " + notValidPokemonName + " not exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnSavedPokemonUpdatePokemon() throws Exception {
        List<String> pokemonTypes = new ArrayList<>();
        pokemonTypes.add("Fire");

        PokemonStats pokemonTypesBefore = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemonBeforeUpdate = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                pokemonTypes,
                pokemonTypesBefore);

        List<String> charizardUpdatedPokemonTypes = new ArrayList<>();
        charizardUpdatedPokemonTypes.add("Fire");
        charizardUpdatedPokemonTypes.add("Flying");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemonAfterUpdate = new Pokemon(
                new ObjectId(),
                1,
                "charizardUpdated",
                "URLUpdated",
                charizardUpdatedPokemonTypes,
                charizardTypesStats);

        String expectedPokemonSelfDeleteLink = "http://localhost/pokemon/" + pokemonAfterUpdate.get_id();

        given(this.pokemonRepository.findById(pokemonAfterUpdate.get_id().toString())).willReturn(Optional.of(pokemonBeforeUpdate));
        given(this.pokemonRepository.save(pokemonAfterUpdate)).willReturn(pokemonAfterUpdate);

        this.mockMvc.perform(
                        put("/pokemon/" + pokemonAfterUpdate.get_id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(pokemonAfterUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", is(pokemonAfterUpdate.get_id())))
                .andExpect(jsonPath("$.name", is("charizardUpdated")))
                .andExpect(jsonPath("$.fotoUrl", is("URLUpdated")))
                .andExpect(jsonPath("$.types", hasSize(2)))
                .andExpect(jsonPath("$.types[0]", is("Fire")))
                .andExpect(jsonPath("$.types[1]", is("Flying")))
                .andExpect(jsonPath("$._links.self.href", is(expectedPokemonSelfDeleteLink)))
                .andExpect(jsonPath("$._links.delete.href", is(expectedPokemonSelfDeleteLink)));
    }

    @Test
    void ShouldReturnStatus404UpdatePokemon() throws Exception {
        List<String> charizardTypes = new ArrayList<>();
        charizardTypes.add("Fire");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemonAfterUpdate = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                charizardTypes,
                charizardTypesStats);

        String falseId = new ObjectId().toString();

        given(this.pokemonRepository.findById(anyString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                        put("/pokemon/" + falseId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(pokemonAfterUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with id: " + falseId + " not exists"));

    }

    @Test
    void ShouldReturnStatus204DeletePokemon() throws Exception {
        List<String> charizardTypes = new ArrayList<>();
        charizardTypes.add("Fire");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                charizardTypes,
                charizardTypesStats);

        given(this.pokemonRepository.findById(anyString())).willReturn(Optional.of(pokemon));

        this.mockMvc.perform(
                        delete("/pokemon/" + pokemon.get_id()))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatus404DeletePokemon() throws Exception {
        List<String> charizardTypes = new ArrayList<>();
        charizardTypes.add("Fire");

        PokemonStats charizardTypesStats = new PokemonStats(
                new Stats(78, 266, 360),
                new Stats(84, 155, 293),
                new Stats(78, 144, 280),
                new Stats(109, 200, 348),
                new Stats(85, 157, 295),
                new Stats(100, 184, 328));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                1,
                "charizard",
                "URL",
                charizardTypes,
                charizardTypesStats);

        given(this.pokemonRepository.findById(anyString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                        delete("/pokemon/" + pokemon.get_id()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon with id: " + pokemon.get_id() + " not exists"));
    }
}