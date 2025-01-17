package org.kunekune.piglioteque.model;

public record MatchDto(Long id, User userOne, User userTwo, Book userOneBook, Book userTwoBook) {
}
