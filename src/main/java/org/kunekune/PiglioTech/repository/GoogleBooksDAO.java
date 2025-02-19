package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.exception.ApiServiceException;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.GoogleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Repository
public class GoogleBooksDAO {

  private final WebClient client;

  public GoogleBooksDAO(String url) {
    client = WebClient.builder().baseUrl(url).filter(errorHandler()).build();
  }

  @Autowired
  public GoogleBooksDAO() {
    client = WebClient.builder().baseUrl("https://www.googleapis.com/books/v1")
        .filter(errorHandler()).build();
  }

  public Book fetchBookByIsbn(String isbn) {
    if (isbn.length() != 10 && isbn.length() != 13) {
      throw new IllegalArgumentException("ISBN must be 10 or 13 characters long");
    }
    return client.get()
        .uri(uriBuilder -> uriBuilder.path("/volumes").queryParam("q", "isbn:" + isbn).build())
        .retrieve().bodyToMono(GoogleResult.class).filter(GoogleResult::containsValidBook)
        .map(GoogleResult::asBook).block();
  }

  private static ExchangeFilterFunction errorHandler() {
    return ExchangeFilterFunction.ofResponseProcessor(r -> {
      if (r.statusCode().is5xxServerError()) {
        return r.bodyToMono(String.class).flatMap(errorBody -> Mono
            .error(new ApiServiceException(errorBody, "Error in Google Books API")));
      } else if (r.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
        return r.bodyToMono(String.class).flatMap(errorBody -> Mono
            .error(new NoSuchElementException("Google books API returned 404 " + errorBody)));
      } else {
        return Mono.just(r);
      }
    });
  }
}
