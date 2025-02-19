package org.kunekune.PiglioTech.service;


import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.*;
import org.kunekune.PiglioTech.repository.MatchRepository;
import org.kunekune.PiglioTech.repository.SwapRequestRepository;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DataJpaTest
class SwapServiceTest {

    @Mock
    private MatchRepository mockMatchRepository;

    @Mock
    private SwapRequestRepository mockSwapRequestRepository;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private SwapServiceImpl swapService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getMatches returns all matches a user is implicated in, in the form of a list of MatchDtos")
    void test_getMatches_happyPath() {
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

        when(mockMatchRepository.findMatchesByUserUid(anyString())).thenReturn(List.of(match));

        List<MatchDto> matches = swapService.getMatches("UID_1");

        assertAll(() -> assertEquals(1, matches.size()),
                () -> assertEquals(1L, matches.getFirst().id()),
                () -> assertEquals(userOne, matches.getFirst().userOne()),
                () -> assertEquals(userTwo, matches.getFirst().userTwo()),
                () -> assertEquals(bookOne, matches.getFirst().userOneBook()),
                () -> assertEquals(bookTwo, matches.getFirst().userTwoBook()));
    }

    @Test
    @DisplayName("getMatches does not return a match if a user has dismissed themselves from a match")
    void test_getMatches_dismissedMatch() {
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
        match.setUserOneDismissed(true);

        when(mockMatchRepository.findMatchesByUserUid(anyString())).thenReturn(List.of(match));

        List<MatchDto> matches = swapService.getMatches("UID_1");

        assertTrue(matches.isEmpty());
    }

    @Test
    @DisplayName("getMatches returns an empty list if no matches are found for a user")
    void test_getMatches_noMatches() {
        when(mockMatchRepository.findMatchesByUserUid(anyString())).thenReturn(List.of());

        List<MatchDto> matches = swapService.getMatches("UID_1");

        assertTrue(matches.isEmpty());
    }


    @Test
    @DisplayName("makeSwapRequest creates, saves and returns a SwapRequest object when provided with a DTO containing valid IDs")
    void test_makeSwapRequest_validDto() {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");

        when(mockSwapRequestRepository.findSwapRequestsByUid(anyString())).thenReturn(List.of());
        when(mockSwapRequestRepository.save(any(SwapRequest.class))).thenAnswer(a -> {
            SwapRequest saved = a.getArgument(0);
            SwapRequest returned =
                    new SwapRequest(saved.getInitiator(), saved.getReceiver(), saved.getReceiverBook());
            returned.setId(1L);
            return returned;
        });

        when(mockUserRepository.findById("UID_1")).thenReturn(Optional
                .of(new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1")));
        when(mockUserRepository.findById("UID_2")).thenAnswer(a -> {
            User user =
                    new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
            user.getBooks().add(new Book("12345", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                    "There can only be one"));
            return Optional.of(user);
        });

        SwapRequest returnedRequest = swapService.makeSwapRequest(dto);

        assertAll(() -> assertEquals(1L, returnedRequest.getId()),
                () -> assertEquals("UID_1", returnedRequest.getInitiator().getUid()),
                () -> assertEquals("UID_2", returnedRequest.getReceiver().getUid()),
                () -> assertEquals("12345", returnedRequest.getReceiverBook().getIsbn()));

        verify(mockSwapRequestRepository, times(1)).save(any(SwapRequest.class));
    }

    @Test
    @DisplayName("makeSwapRequest throws NoSuchElementException if initiator UID is not found in repository")
    void test_makeSwapRequest_initiatorNotFound() {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");

        when(mockSwapRequestRepository.findSwapRequestsByUid(anyString())).thenReturn(List.of());

        when(mockUserRepository.findById("UID_1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> swapService.makeSwapRequest(dto));
    }

    @Test
    @DisplayName("makeSwapRequest throws NoSuchElementException if receiver UID is not found in repository")
    void test_makeSwapRequest_receiverNotFound() {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");

        when(mockSwapRequestRepository.findSwapRequestsByUid(anyString())).thenReturn(List.of());

        when(mockUserRepository.findById("UID_1")).thenReturn(Optional
                .of(new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1")));
        when(mockUserRepository.findById("UID_2")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> swapService.makeSwapRequest(dto));
    }

    @Test
    @DisplayName("makeSwapRequest throws NoSuchElementException if receiver does not have book with provided ISBN")
    void test_makeSwapRequest_invalidIsbn() {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");

        when(mockSwapRequestRepository.findSwapRequestsByUid(anyString())).thenReturn(List.of());

        when(mockUserRepository.findById("UID_1")).thenReturn(Optional
                .of(new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1")));
        when(mockUserRepository.findById("UID_2")).thenAnswer(a -> {
            User user =
                    new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
            user.getBooks().add(new Book("54321", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                    "There can only be one"));
            return Optional.of(user);
        });

        assertThrows(NoSuchElementException.class, () -> swapService.makeSwapRequest(dto));
    }

    @Test
    @DisplayName("makeSwapRequest throws EntityExistsException if identical swap request has already been made")
    void test_makeSwapRequest_alreadyExists() {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");

        when(mockSwapRequestRepository.findSwapRequestsByUid(anyString()))
                .thenReturn(List.of(new SwapRequest(
                        new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1"),
                        new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2"),
                        new Book("12345", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                                "There can only be one"))));

        assertThrows(EntityExistsException.class, () -> swapService.makeSwapRequest(dto));
    }

    @Test
    @DisplayName("makeSwapRequest creates and saves a Match entity if two complementary SwapRequests have been made")
    void test_makeSwapRequest_makesMatch() {
        SwapRequestDto dtoTwo = new SwapRequestDto("UID_2", "UID_1", "54321");

        User userOne =
                new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
        userOne.getBooks().add(new Book("54321", "TITLE 1", "AUTHOR 1", "1900",
                "http://thumbnail.com/3", "There can only be one"));

        User userTwo =
                new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
        userTwo.getBooks().add(new Book("12345", "TITLE 2", "AUTHOR 2", "1900",
                "http://thumbnail.com/4", "Two peas in a pod"));

        when(mockSwapRequestRepository.findSwapRequestsByUid(anyString())).thenAnswer(a -> {
            SwapRequest retrieved = new SwapRequest(userOne, userTwo, userTwo.getBooks().getFirst());
            retrieved.setId(1L);
            return List.of(retrieved);
        });

        when(mockUserRepository.findById("UID_1")).thenReturn(Optional.of(userOne));
        when(mockUserRepository.findById("UID_2")).thenReturn(Optional.of(userTwo));

        swapService.makeSwapRequest(dtoTwo);

        verify(mockMatchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("makeSwapRequest throws EntityExistsException if valid match between two users already exists")
    void test_makeSwapRequest_matchExists() {
        User userOne = new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
        User userTwo = new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");

        Book bookOne = new Book("54321", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3", "There can only be one");
        Book bookTwo = new Book("12345", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4", "Two peas in a pod");

        Match existingMatch = new Match(userOne, userTwo, bookOne, bookTwo);
        when(mockMatchRepository.findMatchesBetweenUsers(anyString(), anyString())).thenReturn(List.of(existingMatch));

        SwapRequestDto request = new SwapRequestDto("UID_1", "UID_2", "12345");

        assertThrows(EntityExistsException.class, () -> swapService.makeSwapRequest(request));
    }


    @Test
    @DisplayName("dismissSwap throws NoSuchElementException if its matchId does not exist")
    void test_dismissSwap_noSuchMatch() {
        SwapDismissal dismissal = new SwapDismissal("UID_1", 1L);

        when(mockMatchRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> swapService.dismissSwap(dismissal));
    }

    @Test
    @DisplayName("dismissSwap throws NoSuchElementException if its userId does not exist for a correct MatchId")
    void test_dismissSwap_userIdDoesNotMatch() {
        User userOne =
                new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
        Book bookOne = new Book("54321", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                "There can only be one");
        User userTwo =
                new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
        Book bookTwo = new Book("12345", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4",
                "Two peas in a pod");
        Match match = new Match(userOne, userTwo, bookOne, bookTwo);
        match.setId(1L);

        SwapDismissal dismissal = new SwapDismissal("UID_5000", 1L);

        when(mockMatchRepository.findById(anyLong())).thenReturn(Optional.of(match));

        assertThrows(NoSuchElementException.class, () -> swapService.dismissSwap(dismissal));
    }

    @Test
    @DisplayName("dismissSwap marks the correct user as having dismissed a swap and saves the matc")
    void test_dismissSwap_correctlyDismisses() {
        User userOne =
                new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
        Book bookOne = new Book("54321", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                "There can only be one");
        User userTwo =
                new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
        Book bookTwo = new Book("12345", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4",
                "Two peas in a pod");
        Match match = new Match(userOne, userTwo, bookOne, bookTwo);
        match.setId(1L);

        SwapDismissal dismissal = new SwapDismissal("UID_1", 1L);

        when(mockMatchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        when(mockMatchRepository.save(match)).thenReturn(match);

        swapService.dismissSwap(dismissal);

        assertTrue(match.isUserOneDismissed());
        assertFalse(match.isUserTwoDismissed());
        verify(mockMatchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("dismissSwap is externally idempotent when already dismissed item is dismissed again")
    void test_dismissSwap_alreadyDismissed() {
        User userOne =
                new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
        Book bookOne = new Book("54321", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                "There can only be one");
        User userTwo =
                new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
        Book bookTwo = new Book("12345", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4",
                "Two peas in a pod");
        Match match = new Match(userOne, userTwo, bookOne, bookTwo);
        match.setId(1L);
        match.setUserOneDismissed(true);

        SwapDismissal dismissal = new SwapDismissal("UID_1", 1L);

        when(mockMatchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        when(mockMatchRepository.save(match)).thenReturn(match);

        swapService.dismissSwap(dismissal);

        assertTrue(match.isUserOneDismissed());
        assertFalse(match.isUserTwoDismissed());
        verify(mockMatchRepository, times(0)).save(any(Match.class));
    }

    @Test
    @DisplayName("dismissSwap deletes a match if two complementary dismissals are made to it")
    void test_dismissSwap_bothUsersDismissed() {
        User userOne =
                new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
        Book bookOne = new Book("54321", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3",
                "There can only be one");
        User userTwo =
                new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2");
        Book bookTwo = new Book("12345", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4",
                "Two peas in a pod");
        Match match = new Match(userOne, userTwo, bookOne, bookTwo);
        match.setId(1L);
        match.setUserOneDismissed(true);

        SwapDismissal dismissal = new SwapDismissal("UID_2", 1L);

        when(mockMatchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        when(mockSwapRequestRepository.findSwapRequestsBetweenUsers(anyString(), anyString()))
                .thenReturn(List.of(new SwapRequest(userOne, userTwo, bookTwo),
                        new SwapRequest(userTwo, userOne, bookOne)));

        swapService.dismissSwap(dismissal);
        verify(mockMatchRepository, times(0)).save(any(Match.class));
        verify(mockMatchRepository, times(1)).delete(any(Match.class));
        verify(mockSwapRequestRepository, times(2)).delete(any(SwapRequest.class));
    }
}
