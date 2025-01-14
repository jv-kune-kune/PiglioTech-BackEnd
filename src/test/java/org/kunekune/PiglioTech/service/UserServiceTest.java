package org.kunekune.PiglioTech.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceTest {

    @Mock
    private UserRepository mockRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    @DisplayName("saveUser returns a new User entity with matching details when provided a valid User entity")
    void test_saveUser_validUser() {
        User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");

        when(mockRepository.save(any(User.class))).thenAnswer(a -> {
            User submittedUser = a.getArgument(0);
            return new User(submittedUser.getUid(), submittedUser.getName(), submittedUser.getEmail(), submittedUser.getRegion(), submittedUser.getThumbnail());
        });

        User returnedUser = userService.saveUser(user);

        assertAll(() -> assertEquals(user.getUid(), returnedUser.getUid()),
                () -> assertEquals(user.getName(), returnedUser.getName()),
                () -> assertEquals(user.getEmail(), returnedUser.getEmail()),
                () -> assertEquals(user.getRegion(), returnedUser.getRegion()),
                () -> assertEquals(user.getThumbnail(), returnedUser.getThumbnail())
        );
    }



    @Test
    @DisplayName("getUserByUid returns a user entity when provided with a UID that exists")
    void test_getUserByUid_uidExists() {
        User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");

        when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByUid("UID");

        assertAll(() -> assertEquals(user.getUid(), foundUser.getUid()),
                () -> assertEquals(user.getName(), foundUser.getName()),
                () -> assertEquals(user.getEmail(), foundUser.getEmail()),
                () -> assertEquals(user.getRegion(), foundUser.getRegion()),
                () -> assertEquals(user.getThumbnail(), foundUser.getThumbnail())
        );
    }

    @Test
    @DisplayName("getUserByUid throws a NoSuchElementException when provided with a UID that does not exist")
    void test_getUserByUid_uidDoesNotExist() {
        when(mockRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserByUid("UID"));
    }



    @Test
    @DisplayName("getUsersByRegion returns a list of all users in a region when provided with a valid region")
    void test_getUsersByRegion_validRegion() {
        User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user3 = new User("UID_3", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        List<User> users = List.of(user1, user2, user3, user4, user5);

        when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(users);

        List<User> returnedUsers = userService.getUsersByRegion(Region.NORTH_WEST);

        assertTrue((returnedUsers.size() == 5));
    }

    @Test
    @DisplayName("getUsersByRegion returns an empty list when provided with an empty region")
    void test_getUsersByRegion_emptyRegion() {
        User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user3 = new User("UID_3", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        List<User> users = List.of(user1, user2, user3, user4, user5);

        when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(List.of());

        List<User> returnedUsers = userService.getUsersByRegion(Region.NORTH_EAST);

        assertTrue((returnedUsers.isEmpty()));
    }

    @Test
    @DisplayName("getUsersByRegionExclude returns a list of all users in a region, without any where the UID = the provided exclude value")
    void test_getUsersByRegionExclude_excludePresent() {
        User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user3 = new User("UID_3", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        List<User> users = List.of(user1, user2, user3, user4, user5);

        when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(users);

        List<User> returnedUsers = userService.getUsersByRegionExclude(Region.NORTH_WEST, "UID_3");

        assertTrue((returnedUsers.size() == 4));
        assertTrue(() -> {
            for (User user : returnedUsers) {
                if (user.getUid().equals("UID_3")) return false;
            }
            return true;
        });
    }

    @Test
    @DisplayName("getUsersByRegionExclude returns a list of all users in a region when the provided exclude value does not match any UIDs")
    void test_getUsersByRegionExclude_excludeAbsent() {
        User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user3 = new User("UID_3", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        List<User> users = List.of(user1, user2, user3, user4, user5);

        when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(users);

        List<User> returnedUsers = userService.getUsersByRegionExclude(Region.NORTH_WEST, "UID_2000");

        assertTrue((returnedUsers.size() == 5));
        assertTrue(() -> {
            for (User user : users) {
                if (user.getUid().equals("UID_2000")) return false;
            }
            return true;
        });
    }
    @Test
    @DisplayName("getUsersByRegionExclude returns an empty list when there are no users in that region")
    void test_getUsersByRegionExclude_emptyRegion() {
        User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user3 = new User("UID_3", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        List<User> users = List.of(user1, user2, user3, user4, user5);

        when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(List.of());

        List<User> returnedUsers = userService.getUsersByRegionExclude(Region.NORTH_EAST, "UID_1");

        assertTrue((returnedUsers.isEmpty()));
    }

    @Test
    @DisplayName("removeBookFromUser successfully removes a book from the user's library")
    void testRemoveBookFromUser_Success() {
        // Arrange
        User user = new User("user1", "John Doe", "john.doe@example.com", Region.NORTH_WEST, "http://thumbnail.com");
        Book book = new Book("9781234567897", "Title", "Author", "2021", "http://thumbnail.com", "Description");
        user.getBooks().add(book); // Add the book to the user's library

        when(mockRepository.findById("user1")).thenReturn(Optional.of(user)); // Mock user retrieval

        userService.removeBookFromUser("user1", "9781234567897");


        assertTrue(user.getBooks().isEmpty(), "The book should be removed from the user's library"); // Validate book removal
        verify(mockRepository, times(1)).save(user); // verify that the user was saved
    }

}