package org.kunekune.PiglioTech.repository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.exception.ApiServiceException;
import org.kunekune.PiglioTech.model.Book;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(GoogleBooksDAO.class)
public class GoogleBooksDaoTest {

    private GoogleBooksDAO googleBooksDAO;
    private MockWebServer mockWebServer;

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

    @Test
    @DisplayName("fetchBookByIsbn returns a Book object matching the returned JSON request body if one is found")
    public void test_fetchBookByIsbn() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setBody(TestStrings.SingleBookJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Book book = googleBooksDAO.fetchBookByIsbn("9780099511120");

        assertAll(() -> assertEquals("9780099511120", book.getIsbn()),
                () -> assertEquals("Jane Eyre", book.getTitle()),
                () -> assertEquals("Charlotte Brontë", book.getAuthor()),
                () -> assertEquals(2007, book.getPublishedYear()),
                () -> assertNotEquals("", book.getDescription()),
                () -> assertNotEquals("", book.getThumbnail())
        );
    }

    @Test
    @DisplayName("fetchBookByIsbn throws NoSuchElementException if no books are returned from API")
    public void test_fetchBookByIsbn_notFound() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setBody(TestStrings.NoBooksJsonResponse)
                .addHeader("Content-Type", "application/json"));

        assertThrows(NoSuchElementException.class, () -> googleBooksDAO.fetchBookByIsbn("9780099511120"));
    }


    // additional tests

    @Test
    @DisplayName("fetchBookByIsbn throws ApiServiceException if the API request times out")
    public void test_fetchBookByIsbn_apiTimeout() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(504) // Gateway Timeout
                .setBody("{\"error\": \"Timeout\"}")
                .addHeader("Content-Type", "application/json"));

        assertThrows(ApiServiceException.class, () -> googleBooksDAO.fetchBookByIsbn("9780099511120"));
    }

    @Test
    @DisplayName("fetchBookByIsbn throws ApiServiceException if the API returns an error")
    public void test_fetchBookByIsbn_apiError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"error\": \"Internal Server Error\"}")
                .addHeader("Content-Type", "application/json"));

        assertThrows(ApiServiceException.class, () -> googleBooksDAO.fetchBookByIsbn("9780099511120"));
    }

    @Test
    @DisplayName("fetchBookByIsbn throws ApiServiceException if the API returns data missing critical fields")
    public void test_fetchBookByIsbn_partialResponse() throws Exception {
        // Partial JSON response with missing author


        mockWebServer.enqueue(new MockResponse()
                .setBody(TestStrings.NoAuthorsResponse)
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setBody(TestStrings.NoTitleResponse)
                .addHeader("Content-Type", "application/json"));

        assertThrows(ApiServiceException.class, () -> googleBooksDAO.fetchBookByIsbn("9780099511120"));
        assertThrows(ApiServiceException.class, () -> googleBooksDAO.fetchBookByIsbn("9780099511120"));
    }

    @Test
    @DisplayName("fetchBookByIsbn throws IllegalArgumentException if the ISBN is empty")
    public void test_fetchBookByIsbn_emptyIsbn() {
        assertThrows(IllegalArgumentException.class, () -> googleBooksDAO.fetchBookByIsbn(""));
    }
}
