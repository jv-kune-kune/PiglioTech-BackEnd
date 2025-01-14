package org.kunekune.PiglioTech.service;

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
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    //TODO: Consider renaming to distinguish
    @Autowired
    private UserRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoogleBooksDAO googleDao;

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
    public User patchUserBooks(String uid, String isbn) {
        User user = repository.findById(uid).orElseThrow(() ->
                new NoSuchElementException("User with UID " + uid + " not found.")
        );

        if (!bookService.isValidIsbn(isbn))
            throw new IllegalArgumentException("Invalid ISBN: " + isbn);

        Book book = bookRepository.findById(isbn).orElseGet(() -> {
            Book fetchedBook = googleDao.fetchBookByIsbn(isbn);
            if (fetchedBook != null)
                bookService.saveBook(fetchedBook);
            return fetchedBook;
        });

        if (book == null)
            throw new IllegalArgumentException("Unable to fetch book with ISBN: " + isbn);

        user.getBooks().add(book);
        repository.save(user);
        return user;
    }

    //TODO: add general patch impl

    //Is valid user user helper method
    @Override
    public boolean isValidUser(User user) {
        return user != null &&
                user.getUid() != null &&
                user.getName() != null &&
                user.getEmail() != null &&
                user.getRegion() != null &&
                user.getThumbnail() != null;
    }

}
