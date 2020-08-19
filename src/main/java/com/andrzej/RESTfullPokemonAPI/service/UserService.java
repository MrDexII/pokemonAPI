package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import com.andrzej.RESTfullPokemonAPI.auth.Role;
import com.andrzej.RESTfullPokemonAPI.exceptions.UserNotFoundException;
import com.andrzej.RESTfullPokemonAPI.repositorie.RoleRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser createUser(ApplicationUser user) {
        Optional<Role> userRole = roleRepository.findByRole("USER");
        ApplicationUser newUser;
        if (userRole.isPresent()) {
            newUser = new ApplicationUser(user.getUsername(), passwordEncoder.encode(user.getPassword()), Set.of(userRole.get()));
        } else throw new RuntimeException("User role not found");
        return userRepository.save(newUser);
    }

    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<ApplicationUser> findUserByName(String name) {
        return userRepository.findByUsername(name);
    }

    public Optional<ApplicationUser> findUserById(String id) {
        return userRepository.findById(Long.valueOf(id));
    }

    public void deleteById(String id) {
        userRepository.deleteById(Long.valueOf(id));
    }

    public ApplicationUser updateUser(String id, ApplicationUser user) {
        Optional<ApplicationUser> userFroDb = findUserById(id);
        if (!userFroDb.isPresent()) {
            throw new UserNotFoundException("User with id: " + id + " not found");
        }
        user.setUser_id(Long.valueOf(id));
        return userRepository.save(user);
    }
}
