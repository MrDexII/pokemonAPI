package com.andrzej.RESTfullPokemonAPI.service;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import com.andrzej.RESTfullPokemonAPI.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> createUser(ApplicationUser user) {
        Optional<ApplicationUser> userExists = userRepository.findByUsername(user.getUsername());
        return userExists.isPresent() ?
                ResponseEntity.status(HttpStatus.CONFLICT).body("User with name " + user.getUsername() + " already exists") :
                ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(new ApplicationUser(user.getUsername(), passwordEncoder.encode(user.getPassword()))));
    }

    public ResponseEntity<List<ApplicationUser>> getAllUsers() {
        List<ApplicationUser> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<?> findUserByName(String name) {
        Optional<ApplicationUser> user = userRepository.findByUsername(name);
        return user.isPresent() ?
                ResponseEntity.ok(user.get()) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with name " + name + " not exists");
    }

    public ResponseEntity<?> findUserById(String id) {
        Optional<ApplicationUser> user = userRepository.findById(Long.valueOf(id));
        return user.isPresent() ?
                ResponseEntity.ok(user.get()) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with name " + id + " not exists");

    }

    public ResponseEntity<?> deleteById(String id) {
        Optional<ApplicationUser> user = userRepository.findById(Long.valueOf(id));
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not exists");
    }

    public ResponseEntity<?> updateUser(String id, ApplicationUser user) {
        Optional<ApplicationUser> userExists = userRepository.findById(Long.valueOf(id));
        return userExists.isPresent() ?
                ResponseEntity.ok(userRepository.save(user)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found");

    }
}
