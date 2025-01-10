package org.kunekune.PiglioTech.model;

public record GoogleResult(int totalItems, GoogleBook[] items) {
    public record GoogleBook(VolumeInfo volumeInfo) {
        public record VolumeInfo(String title, String[] authors,
                                 String publishedDate, String description,
                                 Identifier[] industryIdentifiers, ImageLinks imageLinks) {}
    }
    public record Identifier(String identifier) {}
    public record ImageLinks(String thumbnail) {}

    public static Book asBook(GoogleResult result) {
        return new Book(result.items()[0].volumeInfo().industryIdentifiers()[0].identifier(),
                result.items()[0].volumeInfo().authors()[0],
                Integer.valueOf(result.items()[0].volumeInfo().publishedDate()),
        result.items()[0].volumeInfo().imageLinks().thumbnail(),
        result.items()[0].volumeInfo().description());
    }
}
