package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;

import java.util.Optional;

public interface UserRepository extends DAORepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
}
