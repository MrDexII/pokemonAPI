package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.Role;

import java.util.Optional;

public interface RoleRepository extends DAORepository<Role, Long> {
    Optional<Role> findByRole(String roleName);
}
