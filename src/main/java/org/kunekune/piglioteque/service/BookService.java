package org.kunekune.piglioteque.service;

import org.kunekune.piglioteque.model.Book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Book getBookByIsbn(String isbn);
    boolean isValidIsbn(String isbn);
}
