package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.kunekune.PiglioTech.model.Book;

import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return repository.findById(isbn).orElseThrow(() -> new NoSuchElementException("Book not found with ISBN: " + isbn));
    }
}

