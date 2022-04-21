package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PokemonTypeRepository pokemonTypeRepository;

    @MockBean
    private PokemonRepository pokemonRepository;

    @MockBean
    private MyElasticsearchRepository myElasticsearchRepository;

    private static List<ApplicationUser> users;

    @BeforeAll
    public static void setup() {
        users = new ArrayList<>();

        ApplicationUser adminUser = new ApplicationUser();

        adminUser.setUsername("admin");
        adminUser.setPassword("password");
        adminUser.setAccountNonExpired(true);
        adminUser.setEnabled(true);
        adminUser.setAccountNonLocked(true);
        adminUser.setCredentialsNonExpired(true);
        adminUser.setRoles(null);

        users.add(adminUser);

        ApplicationUser userUser = new ApplicationUser();

        userUser.setUsername("user");
        userUser.setPassword("password");
        userUser.setAccountNonExpired(true);
        userUser.setEnabled(true);
        userUser.setAccountNonLocked(true);
        userUser.setCredentialsNonExpired(true);
        userUser.setRoles(null);

        users.add(userUser);
    }

    @Test
    void UserNamesInSavedUserAndConcreteUserNamesShouldBeEqualFindByRole() {
        userRepository.saveAll(users);

        Optional<ApplicationUser> optionalUser = userRepository.findByUsername(users.get(0).getUsername());
        ApplicationUser applicationUser = optionalUser.orElseGet(ApplicationUser::new);

        assertThat(applicationUser.getUser_id(), equalTo(users.get(0).getUser_id()));
        assertThat(applicationUser.getUsername(), equalTo(users.get(0).getUsername()));
    }
}