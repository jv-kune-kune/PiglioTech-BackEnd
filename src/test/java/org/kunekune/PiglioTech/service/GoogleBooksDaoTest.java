package org.kunekune.PiglioTech.service;

import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.repository.BookRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.kunekune.PiglioTech.service.GoogleBooksDAO;


import java.util.Optional;

@WebFluxTest
@ContextConfiguration (classes = GoogleBooksDAO.class)
public class GoogleBooksDaoTest {

    @Autowired
    private GoogleBooksDAO googleBooksDAO;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void testGetBooksByIsbnFromGoogleBooks() {
        //Arrange
        String isbn = "12345";
        Mockito.when(bookRepository.findById(isbn)).thenReturn(Optional.empty());

        Book mockBook = new Book(isbn, "Test Author", 2022, "http://example.com/thumbnail.jpg", "Test Description");

        // Act
        Mono<Book> bookMono = googleBooksDAO.fetchBookByIsbn(isbn);

        // Assert
        StepVerifier.create(bookMono)
                .expectNextMatches(book -> book.getIsbn().equals("12345") &&
                        book.getAuthor().equals("Test Author") &&
                        book.getPublishedYear().equals(2022))
                .verifyComplete();

    }

}
