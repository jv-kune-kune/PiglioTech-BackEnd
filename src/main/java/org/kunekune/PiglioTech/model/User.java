package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

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
}
