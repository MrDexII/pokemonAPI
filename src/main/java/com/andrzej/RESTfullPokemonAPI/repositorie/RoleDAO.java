package com.andrzej.RESTfullPokemonAPI.repositorie;

import com.andrzej.RESTfullPokemonAPI.auth.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDAO implements RoleRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Role> findByRole(String roleName) {
        String queryString = "SELECT * FROM role r WHERE role =:roleName";
        List<Role> roles = entityManager
                .createNativeQuery(queryString, Role.class)
                .setParameter("roleName", roleName)
                .getResultList();
        return roles.stream().findFirst();
    }

    @Override
    public Role save(Role role) {
        if (role.getRole_id() != null && findById(role.getRole_id()).isPresent())
            entityManager.merge(role);
        else
            entityManager.persist(role);
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {
        String queryString = "SELECT * FROM roles WHERE id=:id";
        List<Role> role = entityManager
                .createNativeQuery(queryString, Role.class)
                .setParameter("id", id)
                .getResultList();
        return role.stream().findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Role> findAll() {
        String queryString = "SELECT * FROM role";
        List<Role> roles = entityManager
                .createNativeQuery(queryString, Role.class)
                .getResultList();
        return roles;
    }

    @Override
    public void delete(Role role) {
        entityManager.remove(role);
    }
}
