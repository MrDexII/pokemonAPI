package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.Role;
import com.andrzej.RESTfullPokemonAPI.elasticsearch.repository.MyElasticsearchRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.initialization-mode=never"})
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private PokemonRepository pokemonRepository;

    @MockBean
    private PokemonTypeRepository pokemonTypeRepository;

    @MockBean
    private MyElasticsearchRepository myElasticsearchRepository;

    private static List<Role> roles;

    @BeforeAll
    public static void setup() {
        roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setRole("ADMIN");

        Role userRole = new Role();
        userRole.setRole("USER");

        roles.add(adminRole);
        roles.add(userRole);
    }

    @Test
    public void RoleNamesInSavedRolesAndConcreteRoleNamesShouldBeEqualFindByRole() {
        roleRepository.saveAll(roles);

        Optional<Role> optionalRole = roleRepository.findByRole(roles.get(0).getRole());
        Role role = optionalRole.orElseGet(Role::new);

        assertThat(role.getRole_id(), equalTo(roles.get(0).getRole_id()));
        assertThat(role.getRole(), equalTo(roles.get(0).getRole()));
    }

}