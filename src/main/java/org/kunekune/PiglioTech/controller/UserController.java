package org.kunekune.PiglioTech.controller;

import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")  // base URL for user-related endpoints

public class UserController {
    @Autowired

    private UserService userService;

    // save a new user
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
       User user = userService.getUserByUid(id);
       return ResponseEntity.status(404).body(null);

        }
    }


