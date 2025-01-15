package org.kunekune.PiglioTech.service;

import jakarta.persistence.EntityExistsException;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BookService bookService;

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getUserByUid(String uid) {
        return repository.findById(uid).orElseThrow();
    }

    @Override
    public List<User> getUsersByRegion(Region region) {
        return repository.getUsersByRegion(region);
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