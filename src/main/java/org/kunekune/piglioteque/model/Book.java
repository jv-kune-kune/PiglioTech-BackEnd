package org.kunekune.piglioteque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    private String isbn;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String publishedYear;
    @Column
    private String thumbnail;
    @Column(columnDefinition = "TEXT")
    private String description;

    public Book() {
    }

    public Book(String isbn, String title, String author, String year, String thumbnail, String description) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishedYear = year;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getPublishedYear() {
        return publishedYear;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public String getDescription() {
        return description;
    }


    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
