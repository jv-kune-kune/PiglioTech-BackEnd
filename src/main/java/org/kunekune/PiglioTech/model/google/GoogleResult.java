package org.kunekune.PiglioTech.model.google;

public record GoogleResult(int totalItems, GoogleBook[] items) {
    public record GoogleBook(VolumeInfo volumeInfo) {
        public record VolumeInfo(String title, String[] authors,
                                 String publishedDate, String description,
                                 Identifier[] industryIdentifiers, ImageLinks imageLinks) {}
    }
    public record Identifier(String identifier) {}
    public record ImageLinks(String thumbnail) {}
}
