package org.kunekune.PiglioTech.service;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.Swap;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.SwapRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
public class SwapServiceTest {

    @Mock
    private SwapRepository mockRepository;

    @InjectMocks
    private SwapServiceImpl swapService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getSwaps returns all swaps for a given user")
    void testGetSwaps() {
        User user1 = new User("user1", "John Doe", "john.doe@example.com", Region.NORTH_WEST, "http://thumbnail.com/1");
        User user2 = new User("user2", "Jane Doe", "jane.doe@example.com", Region.NORTH_WEST, "http://thumbnail.com/2");
        Book book1 = new Book("9781234567897", "Book 1", "Author 1", "2020", "http://thumbnail.com/book1", "Description 1");
        Book book2 = new Book("9780987654321", "Book 2", "Author 2", "2021", "http://thumbnail.com/book2", "Description 2");

        List<Swap> swaps = List.of(new Swap(user1, user2, book1, book2));

        when(mockRepository.findAll_ByRequesterUid_and_ResponderUid("user1", "user2")).thenReturn(swaps);

        List<Swap> result = swapService.getSwaps("user1");

        assertEquals(1, result.size());
        assertEquals(swaps.get(0), result.get(0));
    }


    @Test
    @DisplayName("createSwap saves a new swap and returns it")
    void testCreateSwap() {
        // Arrange: Set up a valid Swap object
        User requester = new User("user1", "John Doe", "john.doe@example.com", Region.NORTH_WEST, "http://thumbnail.com/1");
        User responder = new User("user2", "Jane Doe", "jane.doe@example.com", Region.NORTH_WEST, "http://thumbnail.com/2");
        Book requesterBook = new Book("9781234567897", "Book 1", "Author 1", "2020", "http://thumbnail.com/book1", "Description 1");
        Book responderBook = new Book("9780987654321", "Book 2", "Author 2", "2021", "http://thumbnail.com/book2", "Description 2");

        Swap swap = new Swap(requester, responder, requesterBook, responderBook);

        when(mockRepository.save(swap)).thenReturn(swap);

        Swap result = swapService.createSwap(swap);

        Assert.assertNotNull(result);
        assertEquals(swap, result);
    }
    @Test
    @DisplayName("deleteSwap successfully deletes a swap if it exists")
    void testDeleteSwap_success() {
        // Arrange
        Long swapId = 1L;
        when(mockRepository.existsById(swapId)).thenReturn(true);


        swapService.deleteSwap(swapId);

        verify(mockRepository, times(1)).deleteById(swapId);
    }

    @Test
    @DisplayName("deleteSwap throws IllegalArgumentException if the swap does not exist")
    void testDeleteSwap_nonExistentSwap() {

        Long swapId = 1L;
        when(mockRepository.existsById(swapId)).thenReturn(false);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            swapService.deleteSwap(swapId);
        });

        assertEquals("Swap with ID " + swapId + " does not exist", exception.getMessage());
    }


}
