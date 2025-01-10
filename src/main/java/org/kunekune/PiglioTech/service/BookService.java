package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Book;

public interface BookService {
    Book saveBook(Book book);
    Book getBookByIsbn(String isbn);
}
