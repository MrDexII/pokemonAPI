package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAO implements UserRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ApplicationUser save(ApplicationUser user) {
        if (user.getUser_id() != null && findById(user.getUser_id()).isPresent())
            entityManager.merge(user);
        else
            entityManager.persist(user);
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
    public void delete(ApplicationUser user) {
        entityManager.remove(user);
    }
}
