package org.kunekune.PiglioTech.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.*;

import static org.junit.jupiter.api.Assertions.*;

class SwapConvertersTest {

  @Test
  @DisplayName("matchToDto produces a MatchDto with the same values as the Match it was given")
  void matchToDto() {
    User userOne =
        new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
    User userTwo =
        new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
    Book bookOne = new Book("ISBN_1", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
        "There can only be one");
    Book bookTwo = new Book("ISBN_2", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4",
        "Two peas in a pod");

    Match match = new Match(userOne, userTwo, bookOne, bookTwo);
    match.setId(1L);

    MatchDto dto = SwapConverters.matchToDto(match);

    assertAll(() -> assertEquals(1L, dto.id()), () -> assertEquals(userOne, dto.userOne()),
        () -> assertEquals(userTwo, dto.userTwo()), () -> assertEquals(bookOne, dto.userOneBook()),
        () -> assertEquals(bookTwo, dto.userTwoBook()));
  }
}
