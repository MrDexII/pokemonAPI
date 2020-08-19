package com.andrzej.RESTfullPokemonAPI.repositorie;


import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    ApplicationUser save(ApplicationUser user);

    Optional<ApplicationUser> findById(Long id);

    boolean existsById(Long id);

    List<ApplicationUser> findAll();

    void deleteById(Long id);

    Optional<ApplicationUser> findByUsername(String username);

}
