package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "swap_matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_one_id")
    private User userOne;
    @ManyToOne
    @JoinColumn(name = "user_two_id")
    private User userTwo;

    @ManyToOne
    @JoinColumn(name = "user_one_isbn")
    private Book userOneBook;
    @ManyToOne
    @JoinColumn(name = "user_two_isbn")
    private Book userTwoBook;

    public Match() {}

    public Match(User userOne, User userTwo, Book userOneBook, Book userTwoBook) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.userOneBook = userOneBook;
        this.userTwoBook = userTwoBook;
    }

    public User getUserOne() {
        return userOne;
    }
    public User getUserTwo() {
        return userTwo;
    }
    public Book getUserOneBook() {
        return userOneBook;
    }
    public Book getUserTwoBook() {
        return userTwoBook;
    }

    public void setUserOne(User userOne) {
        this.userOne = userOne;
    }
    public void setUserTwo(User userTwo) {
        this.userTwo = userTwo;
    }
    public void setUserOneBook(Book userOneBook) {
        this.userOneBook = userOneBook;
    }
    public void setUserTwoBook(Book userTwoBook) {
        this.userTwoBook = userTwoBook;
    }
}
