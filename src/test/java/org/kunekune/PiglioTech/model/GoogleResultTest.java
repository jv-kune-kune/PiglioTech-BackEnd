package org.kunekune.PiglioTech.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.google.GoogleResult;

import static org.junit.jupiter.api.Assertions.*;

class GoogleResultTest {

    @Test
    @DisplayName("asBook returns a Book entity with the same details as the GoogleResult object")
    void asBook() {
        GoogleResult bookJson = new GoogleResult(1, new GoogleResult.GoogleBook[]{new GoogleResult.GoogleBook(
                new GoogleResult.GoogleBook.VolumeInfo(
                        "Jane Eyre",
                        new String[] {"Charlotte Brontë"},
                        "2007",
                        "Description",
                        new GoogleResult.Identifier[]{new GoogleResult.Identifier("12345")},
                        new GoogleResult.ImageLinks("https://thumbnail")
                ))});

        Book result = GoogleResult.asBook(bookJson);

        assertAll(() -> assertEquals("12345", result.getIsbn()),
                () -> assertEquals("Charlotte Brontë", result.getAuthor()),
                () -> assertEquals(2007, result.getPublishedYear()),
                () -> assertEquals("https://thumbnail", result.getThumbnail()),
                () -> assertEquals("Description", result.getDescription())
        );
    }
}