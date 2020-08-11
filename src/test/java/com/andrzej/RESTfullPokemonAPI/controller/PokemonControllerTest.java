package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.exceptions.PokemonNotFoundException;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokemonController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class PokemonControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

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

    @MockBean
    private PokemonModelAssembler pokemonModelAssemblerl;

    @Test
    void createPokemon() throws Exception {
        List<PokemonType> pokemonTypes = List.of(new PokemonType(new ObjectId(), "Fire"));
        Pokemon pokemon = new Pokemon(
                new ObjectId(),
                "charizard",
                "URL",
                pokemonTypes);

        EntityModel<Pokemon> pokemonEntityModel = new EntityModel<>(pokemon);
        pokemonEntityModel.add(linkTo(methodOn(PokemonController.class).getPokemonById(pokemon.getId())).withSelfRel());

        String s = objectMapper.writeValueAsString(pokemonEntityModel);

        given(this.pokemonService.isPokemonPresent(pokemon.getPokemonName())).willReturn(false);
        // given(this.pokemonService.createPokemon(pokemon)).willReturn(pokemonEntityModel);

//        MvcResult mvcResult = this.mockMvc.perform(
//                post("/pokemon/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(s))
//                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    void getAllPokemons() {
        List<PokemonType> pokemonTypes = List.of(new PokemonType(new ObjectId(), "Fire"), new PokemonType(new ObjectId(), "Water"));

        List<Pokemon> pokemons = Arrays.asList(
                new Pokemon(
                        new ObjectId(),
                        "Charziazrd",
                        "URL",
                        pokemonTypes),
                new Pokemon(
                        new ObjectId(),
                        "Wartortle",
                        "url",
                        pokemonTypes

                ));

        PageRequest pagination = PageRequest.of(1, 10);
        Page<Pokemon> page = new PageImpl<>(pokemons, pagination, pokemons.size());

        //given(this.pokemonService.getAllPokemons(Mockito.any(PageRequest.class))).willReturn(entityModels);

    }

    @Test
    void getPokemonByIdShouldReturnStatusOKWhenIdIsCorrect() throws Exception {
        List<PokemonType> pokemonTypes = List.of(new PokemonType(new ObjectId(), "Fire"));
        Pokemon pokemon = new Pokemon(new ObjectId(), "charizard", "url", pokemonTypes);

        given(this.pokemonService.getPokemonById(pokemon.getId())).willReturn(new EntityModel<>(pokemon));

        this.mockMvc.perform(
                get("/pokemon/{id}", pokemon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemon.getId())))
                .andExpect(jsonPath("$.pokemonName", is(pokemon.getPokemonName())))
                .andExpect(jsonPath("$.imageUrl", is(pokemon.getImageUrl())))
                .andExpect(jsonPath("$.pokemonType", hasSize(pokemon.getPokemonType().size())));
    }

    @Test
    void getPokemonByIdShouldThrowPokemonNotFoundExceptionWhenIdIsNotCorrect() throws Exception {
        List<PokemonType> pokemonTypes = List.of(new PokemonType(new ObjectId(), "Fire"));
        Pokemon pokemon = new Pokemon(new ObjectId(), "charizard", "url", pokemonTypes);

        String falseID = new ObjectId().toString();
        String exceptionMessage = "Pokemon with id: " + falseID + " not found";

        given(this.pokemonService.getPokemonById(falseID)).willThrow(new PokemonNotFoundException(exceptionMessage));
        //willThrow(new PokemonNotFoundException(exceptionMessage)).given(this.pokemonService.getPokemonById(falseID));
        //.insert(any(String.class), eq(tooLongPhoneNumber));

        this.mockMvc.perform(
                get("/pokemon/{id}", falseID))
                .andDo(print())
                .andExpect(status().is5xxServerError());
        //.andExpect(status().)
    }
}