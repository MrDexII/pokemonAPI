package com.andrzej.RESTfullPokemonAPI.repositorie;

import java.util.List;
import java.util.Optional;

public interface DAORepository<T, E> {

    T save(T user);

    Optional<T> findById(E id);

    boolean existsById(E id);

    List<T> findAll();

    void delete(T user);

    Optional<T> findByUsername(String username);

}
