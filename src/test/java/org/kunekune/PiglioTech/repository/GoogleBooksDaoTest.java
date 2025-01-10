package org.kunekune.PiglioTech.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.google.GoogleResult;
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

        mockWebServer.enqueue(new MockResponse()
                .setBody(TestStrings.singleBookJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<GoogleResult> bookMono = googleBooksDAO.fetchBookByIsbn("9780099511120");


        StepVerifier.create(bookMono)
                .expectNextMatches(b -> b.items()[0].volumeInfo().industryIdentifiers()[0].identifier().equals("9780099511120")
                && b.items()[0].volumeInfo().title().equals("Jane Eyre")
                && b.items()[0].volumeInfo().authors()[0].equals("Charlotte Brontë")
                && b.items()[0].volumeInfo().publishedDate().equals("2007")
                && ! b.items()[0].volumeInfo().description().isEmpty()
                && ! b.items()[0].volumeInfo().imageLinks().thumbnail().isEmpty())
                .verifyComplete();

    }

}
