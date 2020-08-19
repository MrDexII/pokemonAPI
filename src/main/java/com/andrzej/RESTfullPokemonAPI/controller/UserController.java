package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import com.andrzej.RESTfullPokemonAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createUser(@RequestBody ApplicationUser user) {
        if (userService.findUserByName(user.getUsername()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with name " + user.getUsername() + " exists");
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/")
    public ResponseEntity<List<ApplicationUser>> readAllUsers() {
        List<ApplicationUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readOneUserById(@PathVariable("id") String id) {
        Optional<ApplicationUser> user = userService.findUserById(id);
        if (!user.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with name " + id + " not exists");
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<?> readOneUserByName(@PathVariable("name") String name) {
        Optional<ApplicationUser> user = userService.findUserByName(name);
        if (!user.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with name " + name + " not exists");

        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUser> updateUser(@PathVariable("id") String id, @RequestBody ApplicationUser user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        Optional<ApplicationUser> user = userService.findUserById(id);
        if (!user.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: " + id + " not exists");

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
