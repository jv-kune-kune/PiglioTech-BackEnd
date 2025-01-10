package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.repository.BookRepository;
import org.kunekune.PiglioTech.repository.DummyGoogleBooksDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.kunekune.PiglioTech.model.Book;

import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private DummyGoogleBooksDao googleDao;

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        if (repository.existsById(isbn)) {
            return repository.findById(isbn).get(); // Confirmed to exist
        } else {
            Book book = googleDao.getRemoteBookByIsbn(isbn);
            return repository.save(book);
        }
    }
}

