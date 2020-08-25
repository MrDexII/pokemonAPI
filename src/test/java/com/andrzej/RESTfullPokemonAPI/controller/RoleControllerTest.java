package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUserService;
import com.andrzej.RESTfullPokemonAPI.auth.Role;
import com.andrzej.RESTfullPokemonAPI.jwt.JwtConfig;
import com.andrzej.RESTfullPokemonAPI.repositorie.RoleRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import com.andrzej.RESTfullPokemonAPI.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private RoleService roleService;

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
    public void shouldReturnStatusOkAndListOfRoles() throws Exception {
        List<Role> roles = List.of(new Role(1L, "TestRole1"), new Role(2L, "TestRole2"));
        given(this.roleRepository.findAll()).willReturn(roles);

        this.mockMvc.perform(get("/user/role/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roles)));
    }

    @Test
    public void shouldReturnStatusOkAndEmptyList() throws Exception {
        List<Role> roles = List.of();
        given(this.roleRepository.findAll()).willReturn(roles);

        this.mockMvc.perform(get("/user/role/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roles)));
    }
}