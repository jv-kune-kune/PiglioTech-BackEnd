package org.kunekune.PiglioTech.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.exception.GlobalExceptionHandler;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.service.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookControllerTest {

  @Mock
  private BookService mockService;

  @InjectMocks
  private BookController bookController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(bookController)
        .setControllerAdvice(new GlobalExceptionHandler()).build();
  }

  @Test
  @DisplayName("GET /api/v1/books returns all books and HTTP 200")
  void test_getAllBooks() throws Exception {
    // Arrange
    List<Book> books = List.of(
            new Book("1234567890", "Title1", "Author1", "2000", "http://thumbnail1.com", "Description1"),
            new Book("0987654321", "Title2", "Author2", "2010", "http://thumbnail2.com", "Description2")
    );

    when(mockService.getAllBooks()).thenReturn(books);

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(books.get(0).getIsbn()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(books.get(0).getTitle()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].isbn").value(books.get(1).getIsbn()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(books.get(1).getTitle()));
  }

  @Test
  @DisplayName("GET /api/v1/books returns empty list and HTTP 200")
  void test_getAllBooks_emptyList() throws Exception {
    // Arrange
    when(mockService.getAllBooks()).thenReturn(List.of());

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns book details and HTTP 200 if book is found")
  void test_getBookByIsbn_found() throws Exception {
    Book book =
        new Book("1234567890", "Title", "Author", "2000", "http://thumbnail.com", "Description");

    when(mockService.getBookByIsbn(anyString())).thenReturn(book);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/books/1234567890")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.publishedYear").value(book.getPublishedYear()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value(book.getThumbnail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(book.getDescription()));
  }


  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 404 if book is not found")
  void test_getBookByIsbn_notFound() throws Exception {
    when(mockService.getBookByIsbn(anyString())).thenThrow(NoSuchElementException.class);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/books/1234567890")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());


  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for invalid ISBN format")
  void test_getBookByIsbn_invalidFormat() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/books/INVALID_ISBN")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 when ISBN is null")
  void test_getBookByIsbn_nullIsbn() throws Exception {
    // Arrange
    // Nothing specific to mock for this test

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/null")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for ISBN with leading/trailing whitespace")
  void test_getBookByIsbn_withWhitespace() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/ 1234567890 ")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for ISBN with special characters")
  void test_getBookByIsbn_withSpecialCharacters() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/12345@!67890")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for ISBN longer than 13 digits")
  void test_getBookByIsbn_tooLong() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/12345678901234")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for ISBN shorter than 10 digits")
  void test_getBookByIsbn_tooShort() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/12345")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for ISBN with alphabetic characters")
  void test_getBookByIsbn_withAlphabeticCharacters() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/ISBN1234567")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} handles leading zeros in valid ISBNs")
  void test_getBookByIsbn_withLeadingZeros() throws Exception {
    Book book = new Book("0001234567", "Title", "Author", "2000", "http://thumbnail.com", "Description");

    when(mockService.getBookByIsbn("0001234567")).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/0001234567")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()));
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 200 for valid 10-digit ISBN")
  void test_getBookByIsbn_valid10DigitIsbn() throws Exception {
    Book book = new Book("1234567890", "Title", "Author", "2000", "http://thumbnail.com", "Description");

    when(mockService.getBookByIsbn("1234567890")).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1234567890")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()));
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} returns HTTP 200 for valid 13-digit ISBN")
  void test_getBookByIsbn_valid13DigitIsbn() throws Exception {
    Book book = new Book("1234567890123", "Title", "Author", "2000", "http://thumbnail.com", "Description");

    when(mockService.getBookByIsbn("1234567890123")).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1234567890123")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()));
  }

  @Test
  @DisplayName("GET /api/v1/books/{isbn} handles bulk invalid ISBN requests")
  void test_getBookByIsbn_bulkInvalidIsbn() throws Exception {
    String[] invalidIsbns = {"abc123", "short", "longisbn1234567890123", "@!$#isbn"};

    for (String isbn : invalidIsbns) {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/" + isbn)
                      .accept(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
  }
}
