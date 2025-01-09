package org.kunekune.PiglioTech.service;

import java.awt.print.Book;

public interface BookService {
    Book saveBook(Book book);
    Book getBookByIsbn(String isbn);
}
