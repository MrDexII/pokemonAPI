package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.auth.Role;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.RoleRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import com.andrzej.RESTfullPokemonAPI.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @SpyBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ApplicationUserService applicationUserService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private SecretKey secretKey;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private PokemonRepository pokemonRepository;


    @Test
    void ShouldReturnStatus201AndReturnUserWithOneRoleCreateUser() throws Exception {
        Role userRole = new Role(2L, "USER");
        String encodePassword = "123abcEncoded";

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        ApplicationUser userGiven = new ApplicationUser();
        userGiven.setUsername("testUser");
        userGiven.setPassword("123abc");

        ApplicationUser userGivenAfterEncode = new ApplicationUser();
        userGivenAfterEncode.setUsername("testUser");
        userGivenAfterEncode.setPassword(encodePassword);
        userGivenAfterEncode.setRoles(roles);
        userGivenAfterEncode.setAccountNonExpired(true);
        userGivenAfterEncode.setAccountNonLocked(true);
        userGivenAfterEncode.setCredentialsNonExpired(true);
        userGivenAfterEncode.setEnabled(true);

        ApplicationUser userThen = new ApplicationUser();
        userThen.setUser_id(1L);
        userThen.setUsername("testUser");
        userThen.setPassword(encodePassword);
        userThen.setRoles(roles);
        userThen.setAccountNonExpired(true);
        userThen.setAccountNonLocked(true);
        userThen.setCredentialsNonExpired(true);
        userThen.setEnabled(true);

        given(this.userRepository.findByUsername(userGiven.getUsername())).willReturn(Optional.empty());
        given(this.passwordEncoder.encode("123abc")).willReturn(encodePassword);
        given(this.userRepository.save(userGivenAfterEncode)).willReturn(userThen);

        String jsonContent = objectMapper.writeValueAsString(userGiven);

        this.mockMvc.perform(
                        post("/user/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id", is(1)))
                .andExpect(jsonPath("$.username", is("testUser")))
                .andExpect(jsonPath("$.password", is("123abcEncoded")))
                .andExpect(jsonPath("$.authorities", hasSize(1)))
                .andExpect(jsonPath("$.authorities[0]", hasEntry("role", "USER")))
                .andExpect(jsonPath("$.enabled", is(true)))
                .andExpect(jsonPath("$.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.credentialsNonExpired", is(true)));
    }

    @Test
    void ShouldReturnStatus409CreateUser() throws Exception {
        ApplicationUser userGiven = new ApplicationUser();
        userGiven.setUsername("testUser");
        userGiven.setPassword("123abc");

        given(this.userRepository.findByUsername(userGiven.getUsername())).willReturn(Optional.of(userGiven));

        String jsonContent = objectMapper.writeValueAsString(userGiven);

        this.mockMvc.perform(
                        post("/user/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .characterEncoding("utf-8"))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with name " + userGiven.getUsername() + " already exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnListOfAllUsersReadAllUsers() throws Exception {
        List<ApplicationUser> users = new ArrayList<>();
        users.add(new ApplicationUser("testUser1", "testUser1Password"));
        users.add(new ApplicationUser("testUser2", "testUser2Password"));

        given(this.userRepository.findAll()).willReturn(users);

        this.mockMvc.perform(
                        get("/user/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("username", "testUser1")))
                .andExpect(jsonPath("$[0]", hasEntry("password", "testUser1Password")))
                .andExpect(jsonPath("$[0].authorities[0]", hasEntry("role", "USER")))
                .andExpect(jsonPath("$[1]", hasEntry("username", "testUser2")))
                .andExpect(jsonPath("$[1]", hasEntry("password", "testUser2Password")))
                .andExpect(jsonPath("$[1].authorities[0]", hasEntry("role", "USER")));
    }

    @Test
    void ShouldReturnStatus200AndReturnEmptyListOfUsersReadAllUsers() throws Exception {

        given(this.userRepository.findAll()).willReturn(new ArrayList<>());

        this.mockMvc.perform(
                        get("/user/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void ShouldReturnStatus200AndReturnOneUserReadUserById() throws Exception {
        ApplicationUser user = new ApplicationUser("testUser", "testUserPassword");

        given(this.userRepository.findById(anyLong())).willReturn(Optional.of(user));

        this.mockMvc.perform(
                        get("/user/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testUser")))
                .andExpect(jsonPath("$.password", is("testUserPassword")))
                .andExpect(jsonPath("$.authorities", hasSize(1)))
                .andExpect(jsonPath("$.authorities[0]", hasEntry("role", "USER")));
    }

    @Test
    void ShouldReturnStatus404AndReturnNoUserReadUserById() throws Exception {
        given(this.userRepository.findById(anyLong())).willReturn(Optional.empty());

        this.mockMvc.perform(
                        get("/user/123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id " + 123L + " not exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnOneUserReadUserByName() throws Exception {
        ApplicationUser user = new ApplicationUser("testUser", "testUserPassword");

        given(this.userRepository.findByUsername("testUser")).willReturn(Optional.of(user));

        this.mockMvc.perform(
                        get("/user/find").param("name", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testUser")))
                .andExpect(jsonPath("$.password", is("testUserPassword")))
                .andExpect(jsonPath("$.authorities", hasSize(1)))
                .andExpect(jsonPath("$.authorities[0]", hasEntry("role", "USER")));
    }

    @Test
    void ShouldReturnStatus404AndReturnNoUserReadUserByName() throws Exception {
        String username = "testUser";
        given(this.userRepository.findByUsername(username)).willReturn(Optional.empty());

        this.mockMvc.perform(
                        get("/user/find").param("name", username))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with name " + username + " not exists"));
    }

    @Test
    void ShouldReturnStatus200AndReturnUpdatedUserUpdateUser() throws Exception {
        ApplicationUser user = new ApplicationUser("testUser", "testUserPassword");
        ApplicationUser updatedUser = new ApplicationUser("testUpdatedUser", "testUpdatedUserPassword");

        given(this.userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(this.userRepository.save(updatedUser)).willReturn(updatedUser);

        this.mockMvc.perform(
                        put("/user/123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testUpdatedUser")))
                .andExpect(jsonPath("$.password", is("testUpdatedUserPassword")))
                .andExpect(jsonPath("$.authorities", hasSize(1)))
                .andExpect(jsonPath("$.authorities[0]", hasEntry("role", "USER")));
    }

    @Test
    void ShouldReturnStatus404AndReturnNoUserUpdateUser() throws Exception {
        ApplicationUser updatedUser = new ApplicationUser("testUpdatedUser", "testUpdatedUserPassword");
        String id = "123";

        given(this.userRepository.findById(anyLong())).willReturn(Optional.empty());

        this.mockMvc.perform(
                        put("/user/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id " + id + " not exists"));
    }

    @Test
    void ShouldReturnStatus204DeleteUser() throws Exception {
        ApplicationUser user = new ApplicationUser("testUser", "testUserPassword");
        Long id = 123L;

        given(this.userRepository.findById(id)).willReturn(Optional.of(user));

        this.mockMvc.perform(
                        delete("/user/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void ShouldReturnStatus404DeleteUser() throws Exception {
        ApplicationUser user = new ApplicationUser("testUser", "testUserPassword");
        Long id = 123L;

        given(this.userRepository.findById(id)).willReturn(Optional.empty());

        this.mockMvc.perform(
                        delete("/user/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id " + id + " not exists"));
    }
}