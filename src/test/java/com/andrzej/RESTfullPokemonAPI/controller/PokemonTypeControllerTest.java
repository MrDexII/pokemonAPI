package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonTypeModelAssembler;
import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonTypeRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.RoleRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import com.andrzej.RESTfullPokemonAPI.service.PokemonTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonTypeController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class PokemonTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PokemonTypeRepository pokemonTypeRepository;

    @SpyBean
    private PokemonTypeModelAssembler pokemonTypeModelAssembler;

    @SpyBean
    private PokemonTypeService pokemonTypeService;


    //Security Beans
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

    @Test
    void ShouldReturnStatus201AndReturnSavedPokemonCreatePokemonType() throws Exception {
        PokemonType pokemonType = new PokemonType(new ObjectId(), "NORMAL");

        String expectedDeleteSelfLink = "http://localhost/pokemon/type/" + pokemonType.getId();
        String expectedAllPokemonTypesLink = "http://localhost/pokemon/type/";
        given(this.pokemonTypeRepository.save(pokemonType)).willReturn(pokemonType);
        given(this.pokemonTypeRepository.findByName(pokemonType.getName())).willReturn(Optional.empty());

        this.mockMvc.perform(
                post("/pokemon/type/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonType)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(pokemonType.getId())))
                .andExpect(jsonPath("$.name", is("NORMAL")))
                .andExpect(jsonPath("$._links.self.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.deletePokemonType.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.allPokemonTypes.href", is(expectedAllPokemonTypesLink)));
    }

    @Test
    void ShouldReturnStatus409CreatePokemonType() throws Exception {
        PokemonType pokemonType = new PokemonType(new ObjectId(), "NORMAL");

        given(this.pokemonTypeRepository.findByName(pokemonType.getName())).willReturn(Optional.of(pokemonType));

        this.mockMvc.perform(
                post("/pokemon/type/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonType)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Pokemon type with name: " + pokemonType.getName() + " already exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnListOfPokemonTypesGetAllPokemonTypes() throws Exception {
        PokemonType pokemonType1 = new PokemonType(new ObjectId(), "FIRE");
        PokemonType pokemonType2 = new PokemonType(new ObjectId(), "WATER");

        List<PokemonType> listPokemonType = new ArrayList<>();
        listPokemonType.add(pokemonType1);
        listPokemonType.add(pokemonType2);

        String expectedDeleteSelfLink0 = "http://localhost/pokemon/type/" + listPokemonType.get(0).getId();
        String expectedDeleteSelfLink1 = "http://localhost/pokemon/type/" + listPokemonType.get(1).getId();
        String expectedAllPokemonTypesLink = "http://localhost/pokemon/type/";

        given(this.pokemonTypeRepository.findAll()).willReturn(listPokemonType);

        this.mockMvc.perform(
                get("/pokemon/type/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pokemonTypeList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[0].id",
                        is(listPokemonType.get(0).getId())))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[0].name", is("FIRE")))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[0]._links.self.href",
                        is(expectedDeleteSelfLink0)))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[0]._links.deletePokemonType.href",
                        is(expectedDeleteSelfLink0)))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[0]._links.allPokemonTypes.href",
                        is(expectedAllPokemonTypesLink)))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[1].id",
                        is(listPokemonType.get(1).getId())))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[1].name", is("WATER")))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[1]._links.self.href",
                        is(expectedDeleteSelfLink1)))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[1]._links.deletePokemonType.href",
                        is(expectedDeleteSelfLink1)))
                .andExpect(jsonPath("$._embedded.pokemonTypeList[1]._links.allPokemonTypes.href",
                        is(expectedAllPokemonTypesLink)));
    }

    @Test
    void ShouldReturnStatus204GetAllPokemonTypes() throws Exception {
        List<PokemonType> listPokemonType = new ArrayList<>();

        given(this.pokemonTypeRepository.findAll()).willReturn(listPokemonType);

        this.mockMvc.perform(
                get("/pokemon/type/"))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatus200AndReturnPokemonTypeGetPokemonTypeById() throws Exception {
        PokemonType pokemonType = new PokemonType(new ObjectId(), "NORMAL");

        String expectedDeleteSelfLink = "http://localhost/pokemon/type/" + pokemonType.getId();
        String expectedAllPokemonTypesLink = "http://localhost/pokemon/type/";

        given(this.pokemonTypeRepository.findById(pokemonType.getId())).willReturn(Optional.of(pokemonType));

        this.mockMvc.perform(
                get("/pokemon/type/" + pokemonType.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemonType.getId())))
                .andExpect(jsonPath("$.name", is("NORMAL")))
                .andExpect(jsonPath("$._links.self.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.deletePokemonType.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.allPokemonTypes.href", is(expectedAllPokemonTypesLink)));
    }

    @Test
    void ShouldReturnStatus404GetPokemonTypeById() throws Exception {
        String pokemonTypeId = new ObjectId().toString();
        given(this.pokemonTypeRepository.findById(pokemonTypeId)).willReturn(Optional.empty());

        this.mockMvc.perform(
                get("/pokemon/type/" + pokemonTypeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon type with id: " + pokemonTypeId + " not exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnPokemonTypeGetPokemonTypeByName() throws Exception {
        PokemonType pokemonType = new PokemonType(new ObjectId(), "NORMAL");

        String expectedDeleteSelfLink = "http://localhost/pokemon/type/" + pokemonType.getId();
        String expectedAllPokemonTypesLink = "http://localhost/pokemon/type/";

        given(this.pokemonTypeRepository.findByName(pokemonType.getName())).willReturn(Optional.of(pokemonType));

        this.mockMvc.perform(
                get("/pokemon/type/find")
                        .param("name", pokemonType.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemonType.getId())))
                .andExpect(jsonPath("$.name", is("NORMAL")))
                .andExpect(jsonPath("$._links.self.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.deletePokemonType.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.allPokemonTypes.href", is(expectedAllPokemonTypesLink)));
    }

    @Test
    void ShouldReturnStatus404GetPokemonTypeByName() throws Exception {
        String pokemonName = "NORMAL";

        given(this.pokemonTypeRepository.findByName(pokemonName)).willReturn(Optional.empty());

        this.mockMvc.perform(
                get("/pokemon/type/find")
                        .param("name", pokemonName))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon type with name: " + pokemonName + " not exists"));
    }

    @Test
    void ShouldReturnStatus200AndUpdatedPokemonTypeUpdatePokemonType() throws Exception {
        ObjectId pokemonId = new ObjectId();
        PokemonType pokemonType = new PokemonType(pokemonId, "NORMAL");
        PokemonType pokemonTypeAfterUpdate = new PokemonType(pokemonId, "NORMALUpdated");

        String expectedDeleteSelfLink = "http://localhost/pokemon/type/" + pokemonId.toString();
        String expectedAllPokemonTypesLink = "http://localhost/pokemon/type/";

        given(this.pokemonTypeRepository.findById(pokemonId.toString())).willReturn(Optional.of(pokemonType));
        given(this.pokemonTypeRepository.save(pokemonTypeAfterUpdate)).willReturn(pokemonTypeAfterUpdate);

        this.mockMvc.perform(
                put("/pokemon/type/" + pokemonId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonTypeAfterUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pokemonTypeAfterUpdate.getId())))
                .andExpect(jsonPath("$.name", is("NORMALUpdated")))
                .andExpect(jsonPath("$._links.self.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.deletePokemonType.href", is(expectedDeleteSelfLink)))
                .andExpect(jsonPath("$._links.allPokemonTypes.href", is(expectedAllPokemonTypesLink)));
    }

    @Test
    void ShouldReturnStatus404UpdatePokemonType() throws Exception {
        ObjectId pokemonId = new ObjectId();
        PokemonType pokemonTypeAfterUpdate = new PokemonType(pokemonId, "NORMALUpdated");

        given(this.pokemonTypeRepository.findById(pokemonId.toString())).willReturn(Optional.empty());

        this.mockMvc.perform(
                put("/pokemon/type/" + pokemonId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(pokemonTypeAfterUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon type with id: " + pokemonId.toString() + " not exists"));
    }

    @Test
    void ShouldReturnStatus204DeletePokemonType() throws Exception {
        ObjectId pokemonTypeId = new ObjectId();
        PokemonType pokemonType = new PokemonType(pokemonTypeId, "NORMAL");

        given(this.pokemonTypeRepository.findById(pokemonTypeId.toString())).willReturn(Optional.of(pokemonType));

        this.mockMvc.perform(
                delete("/pokemon/type/" + pokemonTypeId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatus404DeletePokemonType() throws Exception {
        String pokemonTypeId = new ObjectId().toString();

        given(this.pokemonTypeRepository.findById(pokemonTypeId)).willReturn(Optional.empty());

        this.mockMvc.perform(
                delete("/pokemon/type/" + pokemonTypeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pokemon type with id: " + pokemonTypeId + " not exists"));
    }
}