package org.kunekune.PiglioTech.model;


import java.awt.*;
import java.util.NoSuchElementException;

public record GoogleResult(int totalItems, GoogleBook[] items) {
    public record GoogleBook(VolumeInfo volumeInfo) {
        public record VolumeInfo(String title, String[] authors,
                                 String publishedDate, String description,
                                 Identifier[] industryIdentifiers, ImageLinks imageLinks) {}
    }
    public record Identifier(String type, String identifier) {}
    public record ImageLinks(String thumbnail) {}

    public Book asBook() {
        if (totalItems < 1) {
            throw new NoSuchElementException("No book found with that ISBN");
        }

        GoogleBook.VolumeInfo volumeInfo = items[0].volumeInfo;

        Identifier[] identifiers = volumeInfo.industryIdentifiers;
        String isbn = null;
        for (Identifier identifier : identifiers) {
            if (identifier.type.equals("ISBN_13")) {
                isbn = identifier.identifier;
                break;
            }
        }
        if (isbn == null) {
            throw new IllegalStateException("Identifier problems: ISBN_13 not located");
        }

        String publishedDate = volumeInfo.publishedDate.substring(0, 4);

        String description = volumeInfo.description;
        description = (description == null) ? "No description provided" : description;
        description = (description.length() > 255) ? description.substring(0, 251) + "..." : description; // H2 varchars are size-limited

        String thumbnail;
        ImageLinks links;
        if ((links = volumeInfo.imageLinks) != null) {
            thumbnail = links.thumbnail;
        } else {
            thumbnail = "https://images.squarespace-cdn.com/content/v1/5add0dce697a985bcc001d2c/1524679485048-OS856WANHVR26WPONXPI/004.JPG";
        }

        return new Book(isbn,
                volumeInfo.title,
                volumeInfo.authors[0],
                Integer.valueOf(publishedDate),
                thumbnail,
                description
        );
    }
}
