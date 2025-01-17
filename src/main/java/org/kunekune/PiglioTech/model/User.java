package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Table(name = "users")
public class User {

  @Id
  private String uid;
  @Column
  private String name;
  @Column
  private String email;
  @Column
  private Region region;
  @Column
  private String thumbnail;

  @ManyToMany
  @JoinTable(name = "users_books", joinColumns = @JoinColumn(name = "users_uid"),
      inverseJoinColumns = @JoinColumn(name = "books_isbn"))
  private List<Book> books;

  public User() {}

  public User(String uid, String name, String email, Region region, String thumbnail) {
    this.uid = uid;
    this.name = name;
    this.email = email;
    this.region = region;
    this.thumbnail = thumbnail;
    this.books = new ArrayList<>();
  }

  public Book getBookByIsbn(String isbn) {
    for (Book book : books) {
      if (book.getIsbn().equals(isbn))
        return book;
    }
    throw new NoSuchElementException("Book with ISBN " + isbn + " not found in user " + uid);
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Region getRegion() {
    return region;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public List<Book> getBooks() {
    return books;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public void setRegion(Region region) {
    this.region = region;
  }
}
