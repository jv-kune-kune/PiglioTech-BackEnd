package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private String phoneNumber;
    @Column
    private Region region;

    @ManyToMany
    @JoinTable(
            name = "users_books",
            joinColumns = @JoinColumn(name = "users_uid"),
            inverseJoinColumns = @JoinColumn(name = "books_isbn")
    )
    private List<Book> books;

    public User() {
    }

    public User(String uid, String name, String email, String phoneNumber, Region region) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.region = region;
        this.books = new ArrayList<>();
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
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Region getRegion() {
        return region;
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
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setRegion(Region region) {
        this.region = region;
    }
}
