package org.kunekune.PiglioTech.service;

import jakarta.persistence.EntityExistsException;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.BookRepository;
import org.kunekune.PiglioTech.repository.GoogleBooksDAO;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoogleBooksDAO googleDao;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookService bookService;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override
    public User getUserByUid(String uid) {
        return userRepository.findById(uid).orElseThrow();
    }

    @Override
    public List<User> getUsersByRegion(Region region) {
        return userRepository.getUsersByRegion(region);
    }

    @Override
    public List<User> getUsersByRegionExclude(Region region, String exclude) {
        List<User> users = new ArrayList<>();
        getUsersByRegion(region).stream()
                .filter(u -> !u.getUid().equals(exclude))
                .forEach(users::add);
        return users;
    }

    @Override
    public User updateUserDetails(String uid, Map<String, Object> updates) {
        // Fetch the user
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + uid + " not found."));

        // Update user fields dynamically
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                case "region":
                    user.setRegion(Region.valueOf((String) value.toString().toUpperCase()));
                    break;
                case "thumbnail":
                    user.setThumbnail((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        // Save the updated user
        return userRepository.save(user);
    }

    // is valid user user helper method
    @Override
    public boolean isValidUser(User user) {
        return user != null &&
                user.getUid() != null &&
                user.getName() != null &&
                user.getEmail() != null &&
                user.getRegion() != null &&
                user.getThumbnail() != null;
    }

    public User addBookToUser(String id, String isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        User user = repository.findById(id).orElseThrow();
        if (user.getBooks().contains(book)) {
            throw new EntityExistsException("User already owns book");
        }
        user.getBooks().add(book);
        return repository.save(user);
    }

    @Override
    public void removeBookFromUser(String userId, String isbn) {
        {
            // get the user
            User user = repository.findById(userId).orElseThrow(() ->
                    new NoSuchElementException("User with ID " + userId + " not found")
            );
            boolean removed = user.getBooks().removeIf(book -> Objects.equals(book.getIsbn(), isbn));

            // in case no book was removed, throw an exception
            if (!removed) {
                throw new NoSuchElementException("Book with ISBN " + isbn + " not found in user's library");
            }
            repository.save(user);

        }
    }
}