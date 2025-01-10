package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.google.GoogleResult;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class GoogleBooksDAO {

    private WebClient client;

    public GoogleBooksDAO(String url) {
        client = WebClient.create(url);
    }

    public GoogleBooksDAO() {
        client = WebClient.create("https://www.googleapis.com/books/v1");
    }

    public Mono<Book> fetchBookByIsbn(String isbn) {
        return client.get().uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", "isbn:" + isbn)
                        .build())
                .retrieve().bodyToMono(GoogleResult.class).map(GoogleResult::asBook);
    }
}
