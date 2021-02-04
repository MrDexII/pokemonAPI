package com.andrzej.RESTfullPokemonAPI.controller;

import com.andrzej.RESTfullPokemonAPI.auth.ApplicationUser;
import com.andrzej.RESTfullPokemonAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody ApplicationUser user) {
        return userService.createUser(user);
    }

    @GetMapping("/")
    public ResponseEntity<List<ApplicationUser>> readAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readUserById(@PathVariable("id") String id) {
        return userService.findUserById(id);
    }

    @GetMapping("/find")
    public ResponseEntity<?> readUserByName(@RequestParam("name") String name) {
        return userService.findUserByName(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody ApplicationUser user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        return userService.deleteById(id);
    }

}
