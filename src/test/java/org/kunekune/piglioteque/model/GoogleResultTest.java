package org.kunekune.piglioteque.model;

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
                () -> assertEquals("2007", result.getPublishedYear()),
                () -> assertEquals("https://thumbnail", result.getThumbnail()),
                () -> assertEquals("Description", result.getDescription())
        );
    }

    @Test
    @DisplayName("asBook throws NoSuchElementException if given a GoogleResult object with empty book array")
    void test_asBook_noBooks() {
        GoogleResult bookJson = new GoogleResult(0, new GoogleResult.GoogleBook[]{});

        assertThrows(NoSuchElementException.class, bookJson::asBook);
    }

   // additional tests - entity functions
    @Test
    @DisplayName("asBook throws IllegalStateException if no ISBN_13 identifier is present")
    void test_asBook_missingIsbn13() {
        GoogleResult bookJson = new GoogleResult(1, new GoogleResult.GoogleBook[]{new GoogleResult.GoogleBook(
                new GoogleResult.GoogleBook.VolumeInfo(
                        "The Hound of the Baskervilles",
                        new String[]{"Arthur Conan Doyle"},
                        "1902",
                        "A gripping tale of mystery and danger.",
                        new GoogleResult.Identifier[]{new GoogleResult.Identifier("ISBN_10", "11111")}, // Only ISBN_10
                        new GoogleResult.ImageLinks("https://example.com/hound.jpg")
                ))});

        assertThrows(IllegalStateException.class, bookJson::asBook);
    }


    @Test
    @DisplayName("asBook sets publishedYear to explanatory string if publishedDate is missing from JSON")
    void test_asBook_missingPublishedDate() {
        GoogleResult bookJson = new GoogleResult(1, new GoogleResult.GoogleBook[]{new GoogleResult.GoogleBook(
                new GoogleResult.GoogleBook.VolumeInfo(
                        "The Hound of the Baskervilles",
                        new String[]{"Arthur Conan Doyle"},
                        null, // Missing publishedDate
                        "A gripping tale of mystery and danger.",
                        new GoogleResult.Identifier[]{new GoogleResult.Identifier("ISBN_13", "12345")},
                        new GoogleResult.ImageLinks("https://example.com/hound.jpg")
                ))});

        Book returnedBook = bookJson.asBook();

        assertEquals("No date available", returnedBook.getPublishedYear());
    }


    @Test
    @DisplayName("asBook replaces 'No description provided' if description is missing")
    void test_asBook_missingDescription() {
        GoogleResult bookJson = new GoogleResult(1, new GoogleResult.GoogleBook[]{new GoogleResult.GoogleBook(
                new GoogleResult.GoogleBook.VolumeInfo(
                        "The Hound of the Baskervilles",
                        new String[]{"Arthur Conan Doyle"},
                        "1902",
                        null, // Missing description
                        new GoogleResult.Identifier[]{new GoogleResult.Identifier("ISBN_13", "12345")},
                        new GoogleResult.ImageLinks("https://example.com/hound.jpg")
                ))});

        Book result = bookJson.asBook();

        assertAll(() -> assertEquals("12345", result.getIsbn()),
                () -> assertEquals("The Hound of the Baskervilles", result.getTitle()),
                () -> assertEquals("Arthur Conan Doyle", result.getAuthor()),
                () -> assertEquals("1902", result.getPublishedYear()),
                () -> assertEquals("https://example.com/hound.jpg", result.getThumbnail()),
                () -> assertEquals("No description provided", result.getDescription()) // Default description
        );
    }

    @Test
    @DisplayName("asBook replaces default thumbnail URL if imageLinks are missing")
    void test_asBook_missingThumbnail() {
        GoogleResult bookJson = new GoogleResult(1, new GoogleResult.GoogleBook[]{new GoogleResult.GoogleBook(
                new GoogleResult.GoogleBook.VolumeInfo(
                        "The Hound of the Baskervilles",
                        new String[]{"Arthur Conan Doyle"},
                        "1902",
                        "A gripping tale of mystery and danger.",
                        new GoogleResult.Identifier[]{new GoogleResult.Identifier("ISBN_13", "12345")},
                        null // Missing imageLinks
                ))});

        Book result = bookJson.asBook();

        assertAll(() -> assertEquals("12345", result.getIsbn()),
                () -> assertEquals("The Hound of the Baskervilles", result.getTitle()),
                () -> assertEquals("Arthur Conan Doyle", result.getAuthor()),
                () -> assertEquals("1902", result.getPublishedYear()),
                () -> assertEquals("https://images.squarespace-cdn.com/content/v1/5add0dce697a985bcc001d2c/1524679485048-OS856WANHVR26WPONXPI/004.JPG", result.getThumbnail()), // Default thumbnail
                () -> assertEquals("A gripping tale of mystery and danger.", result.getDescription())
        );
    }

}


