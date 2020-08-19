package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAO implements UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public ApplicationUser save(ApplicationUser user) {
        if (user.getUser_id() != null) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }
        return user;
    }

    @Override
    public List<ApplicationUser> findAll() {
        String queryString = "SELECT * FROM user";
        List<ApplicationUser> users = entityManager
                .createNativeQuery(queryString, ApplicationUser.class)
                .getResultList();
        return users;
    }

    @Override
    public Optional<ApplicationUser> findById(Long id) {
        String queryString = "SELECT * FROM user WHERE user_id = :id";
        List<ApplicationUser> user = entityManager
                .createNativeQuery(queryString, ApplicationUser.class)
                .setParameter("id", id)
                .getResultList();

        return user.stream().findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<ApplicationUser> findByUsername(String username) {
        String queryString = "SELECT * FROM user WHERE username=:name";
        List<ApplicationUser> user = entityManager
                .createNativeQuery(queryString, ApplicationUser.class)
                .setParameter("name", username)
                .getResultList();
        return user.stream().findFirst();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        String queryString = "DELETE FROM user_authorities WHERE application_user_id = :id";
        entityManager.createNativeQuery(queryString).setParameter("id", id).executeUpdate();
        queryString = "DELETE FROM user WHERE id=:id";
        entityManager.createNativeQuery(queryString).setParameter("id", id).executeUpdate();
    }
}
