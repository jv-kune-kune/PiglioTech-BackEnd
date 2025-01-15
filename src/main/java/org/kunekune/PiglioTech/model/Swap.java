package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "swaps")
public class Swap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "responder_id", nullable = false)
    private User responder;

    @ManyToOne
    @JoinColumn(name = "requester_book_id", nullable = false)
    private Book requesterBook;

    @ManyToOne
    @JoinColumn(name = "responder_book_id",  nullable = false)
    private Book responderBook;

    @Column(nullable = false)
    private boolean accepted = false;

    public Swap () {}

    public Swap(User requester, User responder, Book requesterBook, Book responderBook) {
        this.requester = requester;
        this.responder = responder;
        this.requesterBook = requesterBook;
        this.responderBook= responderBook;
    }

    public Long getId() {
        return id;
    }

    public User getRequester() {
        return requester;
    }

    public User getResponder() {
        return responder;
    }

    public Book getRequesterBook() {
        return requesterBook;
    }

    public Book getResponderBook() {
        return responderBook;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
