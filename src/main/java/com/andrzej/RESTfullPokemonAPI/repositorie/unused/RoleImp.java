package com.andrzej.RESTfullPokemonAPI.repositorie.unused;

import com.andrzej.RESTfullPokemonAPI.auth.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleImp {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public RoleImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Role> findByRole(String roleName) {
        String queryString = "SELECT * FROM role r WHERE role =:roleName";
        List<Role> roles = entityManager
                .createNativeQuery(queryString, Role.class)
                .setParameter("roleName", roleName)
                .getResultList();
        return roles.stream().findFirst();
    }

    public Role save(Role role) {
        if (role.getRole_id() != null)
            entityManager.merge(role);
        else
            entityManager.persist(role);
        return role;
    }

    public Optional<Role> findById(Long id) {
        String queryString = "SELECT * FROM roles WHERE id=:id";
        List<Role> role = entityManager
                .createNativeQuery(queryString, Role.class)
                .setParameter("id", id)
                .getResultList();
        return role.stream().findFirst();
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public List<Role> findAll() {
        String queryString = "SELECT * FROM role";
        List<Role> roles = entityManager
                .createNativeQuery(queryString, Role.class)
                .getResultList();
        return roles;
    }

    public void delete(Role role) {
        entityManager.remove(role);
    }
}
