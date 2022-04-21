package com.andrzej.RESTfullPokemonAPI.auth;

import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationUserService applicationUserService;

    private static ApplicationUser applicationUser;

    @Before
    public void setup() {
        applicationUser = new ApplicationUser();

        Role role1 = new Role(1L, "TestRole1");
        Role role2 = new Role(2L, "TestRole2");

        applicationUser.setUser_id(1L);
        applicationUser.setUsername("testUser");
        applicationUser.setPassword("testPassword");
        applicationUser.setRoles(Set.of(role1, role2));
        applicationUser.setAccountNonExpired(true);
        applicationUser.setAccountNonLocked(true);
        applicationUser.setCredentialsNonExpired(true);
        applicationUser.setEnabled(true);
    }


    @Test
    public void ShouldReturnUserLoadUserByUsername() {
        given(userRepository.findByUsername(applicationUser.getUsername()))
                .willReturn(Optional.of(applicationUser));

        UserDetails testUser = applicationUserService.loadUserByUsername("testUser");

        assertThat(testUser.getUsername(), is(applicationUser.getUsername()));
        assertThat(testUser, is(applicationUser));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void ShouldThrowExceptionLoadUserByUsername() {
        UserDetails testUser = applicationUserService.loadUserByUsername("testUser2");

        assertThat(testUser, is(null));
    }
}