package org.kunekune.PiglioTech.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GoogleResultTest {

    @Test
    @DisplayName("asBook returns a Book entity with the same details as the GoogleResult object")
    void test_asBook() {
        GoogleResult bookJson = new GoogleResult(1, new GoogleResult.GoogleBook[]{new GoogleResult.GoogleBook(
                new GoogleResult.GoogleBook.VolumeInfo(
                        "Jane Eyre",
                        new String[] {"Charlotte Brontë"},
                        "2007",
                        "Description",
                        new GoogleResult.Identifier[]{new GoogleResult.Identifier("ISBN_13", "12345")},
                        new GoogleResult.ImageLinks("https://thumbnail")
                ))});

        Book result = bookJson.asBook();

        assertAll(() -> assertEquals("12345", result.getIsbn()),
                () -> assertEquals("Jane Eyre", result.getTitle()),
                () -> assertEquals("Charlotte Brontë", result.getAuthor()),
                () -> assertEquals(2007, result.getPublishedYear()),
                () -> assertEquals("https://thumbnail", result.getThumbnail()),
                () -> assertEquals("Description", result.getDescription())
        );
    }

    @Test
    @DisplayName("asBook throws NoSuchElementException if given a GoogleResult object with empty book array")
    void test_asBook_noBooks() {
        GoogleResult bookJson = new GoogleResult(0, new GoogleResult.GoogleBook[]{});

        assertThrows(NoSuchElementException.class, () -> bookJson.asBook());
    }
}