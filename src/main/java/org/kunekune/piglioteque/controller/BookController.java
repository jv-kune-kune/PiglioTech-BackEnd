package org.kunekune.piglioteque.controller;

import org.kunekune.piglioteque.model.Book;
import org.kunekune.piglioteque.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping()
    public List<Book> getAllBooks() { return bookService.getAllBooks(); }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        if (isbn.length() != 10 && isbn.length() != 13) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Book book = bookService.getBookByIsbn(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
