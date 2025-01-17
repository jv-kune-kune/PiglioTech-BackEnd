package org.kunekune.piglioteque.controller;

import org.kunekune.piglioteque.model.IsbnDto;
import org.kunekune.piglioteque.model.Region;
import org.kunekune.piglioteque.model.User;
import org.kunekune.piglioteque.service.BookService;
import org.kunekune.piglioteque.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")  // base URL for user-related endpoints
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping()
    public ResponseEntity<List<User>> getUsersByRegion(@RequestParam(required = false) Region region, @RequestParam(required = false) String exclude) {
        if (region != null) {
            if (exclude != null) {
                return new ResponseEntity<>(userService.getUsersByRegionExclude(region, exclude), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(userService.getUsersByRegion(region), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    // get a user by ID
    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserById(@PathVariable String uid) {
        User user = userService.getUserByUid(uid);
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

    @PostMapping("/{id}/books")
    public ResponseEntity<User> addBookToUser(@PathVariable String id, @RequestBody IsbnDto isbn) {
        if (isbn.isbn() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User updatedUser = userService.addBookToUser(id, isbn.isbn());
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/books/{isbn}")
    public ResponseEntity<Void> removeBookFromUser(@PathVariable String id, @PathVariable String isbn) {
        userService.removeBookFromUser(id, isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  
    // update partial user details
    @PatchMapping("/{uid}")
    public ResponseEntity<Map<String, Object>> updateUserDetails(
            @PathVariable String uid,
            @RequestBody Map<String, Object> updates
    ) {
        User updatedUser = userService.updateUserDetails(uid, updates);
        return ResponseEntity.ok(Map.of(
                "message", "User successfully updated",
                "user", updatedUser
        ));
    }
}
