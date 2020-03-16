package com.andrzej.RESTfullPokemonAPI.auth;

import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("MySQL")
public class MySQLApplicationUserDaoService implements ApplicationUserDao {
    private final UserRepository userRepository;

    @Autowired
    public MySQLApplicationUserDaoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
}
