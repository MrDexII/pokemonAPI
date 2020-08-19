package com.andrzej.RESTfullPokemonAPI.auth;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Role")
public class Role implements GrantedAuthority {

    @Id
    private Long role_id;
    private String role;

    public Role() {
    }

    public Role(Long id, String role) {
        this.role_id = id;
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
