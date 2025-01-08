package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    private String isbn;
    @Column
    private String author;
    @Column
    private Integer year;
    @Column
    private String thumbnail;
    @Column
    private String description;

    @ManyToMany(mappedBy = "books")
    private List<User> users;
}
