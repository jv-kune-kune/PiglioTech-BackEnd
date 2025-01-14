package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.repository.BookRepository;
import org.kunekune.PiglioTech.repository.GoogleBooksDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.kunekune.PiglioTech.model.Book;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private GoogleBooksDAO googleDao;

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        if (repository.existsById(isbn)) {
            return repository.findById(isbn).get(); // Confirmed to exist
        } else {
            Book book = googleDao.fetchBookByIsbn(isbn);
            saveBook(book);
            return book;
        }
    }

    @Override
    public boolean isValidIsbn(String isbn) {
        return isbn != null && (isbn.length() == 10 || isbn.length() == 13) && isbn.matches("\\d+");
    }
}
