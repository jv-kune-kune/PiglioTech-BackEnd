package org.kunekune.PiglioTech.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @DisplayName("GET/api/v1/books/{isbn} returns book details and HTTP 200 if book is found")
    void test_getBookByIsbn_found() throws Exception {
        Book book = new Book("1234567890", "Author", 2000, "http://thumbnail.com", "Description");

        when(mockService.getBookByIsbn(anyString())).thenReturn(book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books/1234567890")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedYear").value(book.getPublishedYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value(book.getThumbnail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(book.getDescription()));
    }


    @Test
    @DisplayName("GET /api/v1/books/{isbn} returns HTTP 404 if book is not found")
    void test_getBookByIsbn_notFound() throws Exception {
        when(mockService.getBookByIsbn(anyString())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/books/1234567890")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }
    @Test
    @DisplayName("GET /api/v1/books/{isbn} returns HTTP 400 for invalid ISBN format")
    void test_getBookByIsbn_invalidFormat() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/books/INVALID_ISBN")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



    @Test
    @DisplayName("GET /api/v1/books/{isbn} returns HTTP 500 for unexpected service failure")
    void test_getBookByIsbn_serviceFailure() throws Exception {
        when(mockService.getBookByIsbn(anyString())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/books/1234567890")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}

