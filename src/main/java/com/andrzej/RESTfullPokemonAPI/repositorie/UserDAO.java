package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class UserDAO implements UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public ApplicationUser save(ApplicationUser user) {

        entityManager.persist(user);

        return user;
    }

    @Override
    public Optional<ApplicationUser> findById(Long id) {
        String queryString = "SELECT * " +
                "FROM user" +
                "WHERE id=?id";

        return entityManager.createQuery(queryString, ApplicationUser.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public boolean existsById(Long var1) {
        return false;
    }

    @Override
    public List<ApplicationUser> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }

    @Override
    public Optional<ApplicationUser> findByUsername(String username) {
        String queryString = "SELECT * FROM user WHERE username=:name";

        List<ApplicationUser> name = entityManager.createNativeQuery(queryString, ApplicationUser.class)
                .setParameter("name", username)
                .getResultList();

        return Optional.of(name.get(0));
    }

    @Override
    public void delete(ApplicationUser user) {

    }
}
