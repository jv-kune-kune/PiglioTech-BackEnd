package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Book;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GoogleBooksDAO {

    public Mono<Book> fetchBookByIsbn(String isbn) {
        // return empty - test
        return Mono.empty();

    }
}
