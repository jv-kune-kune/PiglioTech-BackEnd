package org.kunekune.PiglioTech.model;

import jakarta.persistence.EntityNotFoundException;

public record GoogleResult(int totalItems, GoogleBook[] items) {
    public record GoogleBook(VolumeInfo volumeInfo) {
        public record VolumeInfo(String title, String[] authors,
                                 String publishedDate, String description,
                                 Identifier[] industryIdentifiers, ImageLinks imageLinks) {}
    }
    public record Identifier(String identifier) {}
    public record ImageLinks(String thumbnail) {}

    public static Book asBook(GoogleResult result) {
        if (result.totalItems() < 1) {
            throw new EntityNotFoundException("No book found with that ISBN");
        }

        String description = result.items()[0].volumeInfo().description(); // H2 varchars are size-limited
        boolean long_desc =  (description.length() > 255);

        return new Book(result.items()[0].volumeInfo().industryIdentifiers()[0].identifier(),
                result.items()[0].volumeInfo().authors()[0],
                Integer.valueOf(result.items()[0].volumeInfo().publishedDate()),
        result.items()[0].volumeInfo().imageLinks().thumbnail(),
        long_desc ? description.substring(0, 251) + "..." : description);
    }
}
