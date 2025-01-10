package org.kunekune.PiglioTech.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.Optional;

@WebFluxTest(GoogleBooksDAO.class)
public class GoogleBooksDaoTest {

    private GoogleBooksDAO googleBooksDAO;
    private MockWebServer mockWebServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        googleBooksDAO = new GoogleBooksDAO(mockWebServer.url("/").toString());
    }

    @AfterEach
    void teardown() throws Exception {
        mockWebServer.shutdown();
    }

    // Basic test with fake data (does not resemble Google data) to get used to WebFlux testing
    @Test
    public void initialTest() throws Exception {
        String isbn = "12345";

        Book mockBook = new Book(isbn, "Test Author", 2022, "http://example.com/thumbnail.jpg", "Test Description");
        String bookJson = mapper.writeValueAsString(mockBook);

        mockWebServer.enqueue(new MockResponse()
                .setBody(bookJson)
                .addHeader("Content-Type", "application/json"));

        Mono<Book> bookMono = googleBooksDAO.fetchBookByIsbn(isbn);


        StepVerifier.create(bookMono)
                .expectNextMatches(book -> book.getIsbn().equals("12345") &&
                        book.getAuthor().equals("Test Author") &&
                        book.getPublishedYear().equals(2022))
                .verifyComplete();

    }

}
