package com.andrzej.RESTfullPokemonAPI.repositorie.unused;

import java.util.List;
import java.util.Optional;

public interface MyRepository<T, E> {

    T save(T element);

    Optional<T> findById(E id);

    boolean existsById(E id);

    List<T> findAll();

    void delete(T element);

}
