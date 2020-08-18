package com.andrzej.RESTfullPokemonAPI.repositorie;


import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    ApplicationUser save(ApplicationUser var1);

    Optional<ApplicationUser> findById(Long var1);

    boolean existsById(Long var1);

    List<ApplicationUser> findAll();

    void deleteById(Long var1);

    Optional<ApplicationUser> findByUsername(String username);

    void delete(com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser user);
}
