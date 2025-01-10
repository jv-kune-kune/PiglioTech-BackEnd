package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class DummyGoogleBooksDao {
    public Book getRemoteBookByIsbn(String isbn) {
        return new Book("1234567890", "AUTHOR", 1900, "https://thumbnail.com", "A book");
    }
}
