package org.kunekune.PiglioTech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "swap_requests")
public class SwapRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "receiver_book_isbn")
    private Book receiverBook;

    public SwapRequest() {}

    public SwapRequest(User initiator, User receiver, Book receiverBook) {
        this.initiator = initiator;
        this.receiver = receiver;
        this.receiverBook = receiverBook;
    }

    public Long getId() {
        return id;
    }
    public User getInitiator() {
        return initiator;
    }
    public User getReceiver() {
        return receiver;
    }
    public Book getReceiverBook() {
        return receiverBook;
    }

    public void setId(Long id) {
        this.id = id;
    } // For testing
    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    public void setReceiverBook(Book receiverBook) {
        this.receiverBook = receiverBook;
    }
}
