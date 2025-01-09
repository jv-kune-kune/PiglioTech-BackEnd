package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override getBookByIsbn(String isbn) {
        return repository.findById(isbn).orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        }
    }

