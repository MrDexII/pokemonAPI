package com.andrzej.RESTfullPokemonAPI.elasticsearch.controller;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.elasticsearch.repository.MyElasticsearchRepository;
import com.andrzej.RESTfullPokemonAPI.elasticsearch.service.ElasticsearchService;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonStats;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonTypeRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ElasticsearchController.class)
@AutoConfigureMockMvc(addFilters = false)
class ElasticsearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PagedResourcesAssembler<Pokemon> pagedResourcesAssembler;

    @SpyBean
    private PokemonModelAssembler modelAssembler;

    @MockBean
    private ElasticsearchService elasticsearchService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private SecretKey secretKey;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private PokemonRepository pokemonRepository;

    @MockBean
    private PokemonTypeRepository pokemonTypeRepository;

    @MockBean
    private MyElasticsearchRepository myElasticsearchRepository;

    private static List<Pokemon> pokemons;

    @BeforeAll
    static void setup() {
        pokemons = new ArrayList<>();

        Pokemon charizard = new Pokemon(
                new ObjectId(),
                1,
                "Charizard",
                "CharizardUrl",
                List.of("fire", "flaying"),
                new PokemonStats());
        Pokemon charmander = new Pokemon(
                new ObjectId(),
                2,
                "Charmander",
                "CharmanderUrl",
                List.of("Fire"),
                new PokemonStats());

        pokemons.add(charizard);
        pokemons.add(charmander);
    }

    @Test
    void shouldReturnStatus200GetPokemonByNameSearchAsYouType() throws Exception {
        Page<Pokemon> pokemonPage = new PageImpl<>(pokemons);
        PagedModel<EntityModel<Pokemon>> pagedPokemonsModel = pagedResourcesAssembler.toModel(pokemonPage, Link.of("http://localhost:8080/tets"));
        ResponseEntity<PagedModel<EntityModel<Pokemon>>> response = ResponseEntity.ok(pagedPokemonsModel);

        given(this.elasticsearchService.searchAsYouType("char", PageRequest.of(0, 10))).willReturn(response);

        ResultActions resultActions = this.mockMvc
                .perform(get("/elastic/char")
                        .param("page", "0")
                        .param("size", "10"));
        resultActions.andExpect(status().isOk());
        verifyJson(resultActions);
    }

    private void verifyJson(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath("$._embedded.pokemonList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemonList[0]._id", is(pokemons.get(0).get_id())))
                .andExpect(jsonPath("$._embedded.pokemonList[0].number", is(pokemons.get(0).getNumber())))
                .andExpect(jsonPath("$._embedded.pokemonList[0].name", is(pokemons.get(0).getName())))
                .andExpect(jsonPath("$._embedded.pokemonList[0].fotoUrl", is(pokemons.get(0).getFotoUrl())))
                .andExpect(jsonPath("$._embedded.pokemonList[0].types", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemonList[0].types[0]", is(pokemons.get(0).getTypes().get(0))))
                .andExpect(jsonPath("$._embedded.pokemonList[0].types[1]", is(pokemons.get(0).getTypes().get(1))))
                .andExpect(jsonPath("$._embedded.pokemonList[1]._id", is(pokemons.get(1).get_id())))
                .andExpect(jsonPath("$._embedded.pokemonList[1].number", is(pokemons.get(1).getNumber())))
                .andExpect(jsonPath("$._embedded.pokemonList[1].name", is(pokemons.get(1).getName())))
                .andExpect(jsonPath("$._embedded.pokemonList[1].fotoUrl", is(pokemons.get(1).getFotoUrl())))
                .andExpect(jsonPath("$._embedded.pokemonList[1].types", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pokemonList[1].types[0]", is(pokemons.get(1).getTypes().get(0))))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost:8080/tets")))
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    void shouldReturnStatus200IfContentIsNullGetPokemonByNameSearchAsYouType() throws Exception {
        Page<Pokemon> pokemonPage = new PageImpl<>(List.of());
        PagedModel<EntityModel<Pokemon>> pagedPokemonsModel = pagedResourcesAssembler.toModel(pokemonPage, Link.of("http://localhost:8080/tets"));
        ResponseEntity<PagedModel<EntityModel<Pokemon>>> response = ResponseEntity.ok(pagedPokemonsModel);

        given(this.elasticsearchService
                .searchAsYouType("char", PageRequest.of(0, 10)))
                .willReturn(response);

        ResultActions resultActions = this.mockMvc
                .perform(get("/elastic/char")
                        .param("page", "0")
                        .param("size", "10"));
        resultActions.andExpect(status().isOk());
    }
}