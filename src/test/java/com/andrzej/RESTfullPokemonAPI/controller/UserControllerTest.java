package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.auth.Role;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
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
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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


    @Test
    void ShouldReturnStatus201AndUserWithOneRole() throws Exception {
        Role userRole = new Role(2L, "USER");
        String encodePassword = "123abcEncoded";

        ApplicationUser userGiven = new ApplicationUser();
        userGiven.setUsername("testUser");
        userGiven.setPassword("123abc");

        ApplicationUser userGivenAfterEncode = new ApplicationUser();
        userGivenAfterEncode.setUsername("testUser");
        userGivenAfterEncode.setPassword(encodePassword);
        userGivenAfterEncode.setRoles(Set.of(userRole));
        userGivenAfterEncode.setAccountNonExpired(true);
        userGivenAfterEncode.setAccountNonLocked(true);
        userGivenAfterEncode.setCredentialsNonExpired(true);
        userGivenAfterEncode.setEnabled(true);

        ApplicationUser userThen = new ApplicationUser();
        userThen.setUser_id(1L);
        userThen.setUsername("testUser");
        userThen.setPassword(encodePassword);
        userThen.setRoles(Set.of(userRole));
        userThen.setAccountNonExpired(true);
        userThen.setAccountNonLocked(true);
        userThen.setCredentialsNonExpired(true);
        userThen.setEnabled(true);

        given(this.userRepository.findByUsername(userGiven.getUsername())).willReturn(Optional.empty());
        given(this.passwordEncoder.encode("123abc")).willReturn(encodePassword);
        given(this.userRepository.save(userGivenAfterEncode)).willReturn(userThen);

        String jsonContent = objectMapper.writeValueAsString(userGiven);

        this.mockMvc.perform(
                post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id", is(1)))
                .andExpect(jsonPath("$.username", is("testUser")))
                .andExpect(jsonPath("$.password", is("123abcEncoded")))
                .andExpect(jsonPath("$.authorities", hasSize(1)))
                .andExpect(jsonPath("$.enabled", is(true)))
                .andExpect(jsonPath("$.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.credentialsNonExpired", is(true)));
    }

    @Test
    void ShouldReturnStatus409() throws Exception {
        ApplicationUser userGiven = new ApplicationUser();
        userGiven.setUsername("testUser");
        userGiven.setPassword("123abc");

        given(this.userRepository.findByUsername(userGiven.getUsername())).willReturn(Optional.of(userGiven));

        String jsonContent = objectMapper.writeValueAsString(userGiven);

        this.mockMvc.perform(
                post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .characterEncoding("utf-8"))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with name " + userGiven.getUsername() + " already exists"));
    }

    @Test
    void readAllUsers() {
    }

    @Test
    void readUserById() {
    }

    @Test
    void readOneUserByName() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}