package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private Integer publishedYear;
    @Column
    private String thumbnail;
    @Column
    private String description;

    @ManyToMany(mappedBy = "books")
    private List<User> users;

    public Book() {
    }

    public Book(String isbn, String title, String author, Integer year, String thumbnail, String description) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishedYear = year;
        this.thumbnail = thumbnail;
        this.description = description;
        this.users = new ArrayList<>();
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
    public Integer getPublishedYear() {
        return publishedYear;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public String getDescription() {
        return description;
    }
    public List<User> getUsers() {
        return users;
    }


    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
