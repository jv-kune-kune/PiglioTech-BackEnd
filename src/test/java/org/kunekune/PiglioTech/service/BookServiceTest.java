package org.kunekune.PiglioTech.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.BookRepository;
import org.kunekune.PiglioTech.repository.GoogleBooksDAO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DataJpaTest
class BookServiceTest {

  @Mock
  private BookRepository mockRepository;

  @Mock
  private GoogleBooksDAO googleDao;

  @InjectMocks
  private BookService bookService = new BookServiceImpl();

  @Test
  @DisplayName("saveBook returns a new Book entity with matching details when provided a valid Book entity")
  void test_saveBook_validBook() {
    Book book = new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com", "A book");

    when(mockRepository.save(any(Book.class))).thenAnswer(a -> {
      Book submittedBook = a.getArgument(0);
      return new Book(submittedBook.getIsbn(), submittedBook.getTitle(), submittedBook.getAuthor(),
          submittedBook.getPublishedYear(), submittedBook.getThumbnail(),
          submittedBook.getDescription());
    });

    Book returnedBook = bookService.saveBook(book);

    assertAll(() -> assertEquals(book.getIsbn(), returnedBook.getIsbn()),
        () -> assertEquals(book.getTitle(), returnedBook.getTitle()),
        () -> assertEquals(book.getAuthor(), returnedBook.getAuthor()),
        () -> assertEquals(book.getPublishedYear(), returnedBook.getPublishedYear()),
        () -> assertEquals(book.getPublishedYear(), returnedBook.getPublishedYear()),
        () -> assertEquals(book.getDescription(), returnedBook.getDescription()));
  }



  @Test
  @DisplayName("getBookByIsbn returns a Book entity when provided with an ISBN that exists in local database")
  void test_getBookByIsbn_isbnExistsLocally() {
    Book book = new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com", "A book");

    when(mockRepository.findById(anyString()))
        .thenReturn(Optional.of(new Book(book.getIsbn(), book.getTitle(), book.getAuthor(),
            book.getPublishedYear(), book.getThumbnail(), book.getDescription())));
    when(mockRepository.existsById(anyString())).thenReturn(true);

    Book foundBook = bookService.getBookByIsbn("1234567890");

    assertAll(() -> assertEquals(book.getIsbn(), foundBook.getIsbn()),
        () -> assertEquals(book.getTitle(), foundBook.getTitle()),
        () -> assertEquals(book.getAuthor(), foundBook.getAuthor()),
        () -> assertEquals(book.getPublishedYear(), foundBook.getPublishedYear()),
        () -> assertEquals(book.getThumbnail(), foundBook.getThumbnail()),
        () -> assertEquals(book.getDescription(), foundBook.getDescription()));
  }

  @Test
  @DisplayName("getBookByIsbn returns a Book entity when provided with an ISBN that does not exist in local database, but does exist remotely")
  void test_getBookByIsbn_isbnExistsRemotely() {
    Book book = new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com", "A book");

    when(mockRepository.findById(anyString())).thenReturn(Optional.empty());
    when(googleDao.fetchBookByIsbn(anyString()))
        .thenReturn(new Book(book.getIsbn(), book.getTitle(), book.getAuthor(),
            book.getPublishedYear(), book.getThumbnail(), book.getDescription()));
    when(mockRepository.existsById(anyString())).thenReturn(false);
    when(mockRepository.save(any(Book.class))).thenAnswer(a -> {
      Book savedBook = a.getArgument(0);
      return new Book(savedBook.getIsbn(), savedBook.getTitle(), savedBook.getAuthor(),
          savedBook.getPublishedYear(), savedBook.getThumbnail(), savedBook.getDescription());
    });

    Book foundBook = bookService.getBookByIsbn("1234567890");

    assertAll(() -> assertEquals(book.getIsbn(), foundBook.getIsbn()),
        () -> assertEquals(book.getTitle(), foundBook.getTitle()),
        () -> assertEquals(book.getAuthor(), foundBook.getAuthor()),
        () -> assertEquals(book.getPublishedYear(), foundBook.getPublishedYear()),
        () -> assertEquals(book.getThumbnail(), foundBook.getThumbnail()),
        () -> assertEquals(book.getDescription(), foundBook.getDescription()));

    verify(mockRepository, times(1)).save(any(Book.class));
  }

  @Test
  @DisplayName("getBookByIsbn throws a NoSuchElementException when provided with an ISBN that does not exist either locally or remotely")
  void test_getBookByIsbn_isbnDoesNotExist() {
    when(mockRepository.findById(anyString())).thenReturn(Optional.empty());
    when(mockRepository.existsById(anyString())).thenReturn(false);
    when(googleDao.fetchBookByIsbn(anyString())).thenThrow(NoSuchElementException.class);

    assertThrows(NoSuchElementException.class, () -> bookService.getBookByIsbn("1234567890"));
  }
}
