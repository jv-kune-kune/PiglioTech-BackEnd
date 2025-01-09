package org.kunekune.PiglioTech.controller;

import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")  // base URL for user-related endpoints
public class UserController {

    @Autowired
    private UserService userService;

    // get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserByUid(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // save a new user
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        if (user.getUid() == null || user.getName() == null || user.getEmail() == null || user.getRegion() == null || user.getThumbnail() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}


