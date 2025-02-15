package org.kunekune.PiglioTech.service;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceTest {

  @Mock
  private UserRepository mockRepository;

  @Mock
  private BookService mockBookService;

  @InjectMocks
  private UserService userService = new UserServiceImpl();

  @Test
  @DisplayName("getAllUsers returns a list of all users in repository")
  void test_getAllUsers() {
    User userOne =
        new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
    User userTwo =
        new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_EAST, "http://thumbnail.com/2");

    when(mockRepository.findAll()).thenReturn(List.of(userOne, userTwo));

    List<User> users = userService.getAllUsers();

    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("UID_1", users.getFirst().getUid()),
        () -> assertEquals("UID_2", users.get(1).getUid()));
  }

  @Test
  @DisplayName("getAllUsers returns an empty list when there are no users in repository")
  void test_getAllUsers_noBooks() {
    when(mockRepository.findAll()).thenReturn(List.of());

    List<User> users = userService.getAllUsers();

    assertEquals(0, users.size());
  }



  @Test
  @DisplayName("saveUser returns a new User entity with matching details when provided a valid User entity")
  void test_saveUser_validUser() {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");

    when(mockRepository.save(any(User.class))).thenAnswer(a -> {
      User submittedUser = a.getArgument(0);
      return new User(submittedUser.getUid(), submittedUser.getName(), submittedUser.getEmail(),
          submittedUser.getRegion(), submittedUser.getThumbnail());
    });

    User returnedUser = userService.saveUser(user);

    assertAll(() -> assertEquals(user.getUid(), returnedUser.getUid()),
        () -> assertEquals(user.getName(), returnedUser.getName()),
        () -> assertEquals(user.getEmail(), returnedUser.getEmail()),
        () -> assertEquals(user.getRegion(), returnedUser.getRegion()),
        () -> assertEquals(user.getThumbnail(), returnedUser.getThumbnail()));
  }

  @Test
  @DisplayName("saveUser throws IllegalArgumentException when user is null")
  void test_saveUser_nullUser() {
    assertThrows(IllegalArgumentException.class, () -> userService.saveUser(null));
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
        () -> assertEquals(user.getThumbnail(), foundUser.getThumbnail()));
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
    when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(List.of());

    List<User> returnedUsers = userService.getUsersByRegion(Region.NORTH_EAST);

    assertTrue((returnedUsers.isEmpty()));
  }

  @Test
  @DisplayName("getUsersByRegion throws IllegalArgumentException for null region")
  void test_getUsersByRegion_nullRegion() {
    assertThrows(IllegalArgumentException.class, () -> userService.getUsersByRegion(null));
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
        if (user.getUid().equals("UID_3"))
          return false;
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
        if (user.getUid().equals("UID_2000"))
          return false;
      }
      return true;
    });
  }

  @Test
  @DisplayName("getUsersByRegionExclude returns an empty list when there are no users in that region")
  void test_getUsersByRegionExclude_emptyRegion() {

    when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(List.of());

    List<User> returnedUsers = userService.getUsersByRegionExclude(Region.NORTH_EAST, "UID_1");

    assertTrue((returnedUsers.isEmpty()));
  }

  @Test
  @DisplayName("getUsersByRegionExclude ignores empty exclude value and returns all users")
  void test_getUsersByRegionExclude_emptyExclude() {
    User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    List<User> users = List.of(user1, user2);

    when(mockRepository.getUsersByRegion(any(Region.class))).thenReturn(users);

    List<User> returnedUsers = userService.getUsersByRegionExclude(Region.NORTH_WEST, "");

    assertEquals(2, returnedUsers.size());
    assertTrue(returnedUsers.contains(user1));
    assertTrue(returnedUsers.contains(user2));
  }



  @Test
  @DisplayName("addBookToUser returns a user object with a book added to it when both supplied user ID and book ISBN are valid")
  void test_addBookToUser_validIds() {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    Book book = new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com", "A book");
    when(mockBookService.getBookByIsbn(anyString())).thenReturn(book);
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(mockRepository.save(any(User.class))).thenAnswer(a -> {
      User savedUser = a.getArgument(0);
      User newUser = new User(savedUser.getUid(), savedUser.getName(), savedUser.getEmail(),
          savedUser.getRegion(), savedUser.getThumbnail());
      newUser.getBooks().addAll(savedUser.getBooks());
      return newUser;
    });

    User updatedUser = userService.addBookToUser("UID", "1234567890");

    assertAll(() -> assertFalse(updatedUser.getBooks().isEmpty()),
        () -> assertEquals("TITLE", updatedUser.getBooks().getFirst().getTitle()));
  }

  @Test
  @DisplayName("addBookToUser throws NoSuchElementException when provided a UID that does not exist in database")
  void test_addBookToUser_invalidUid() {
    when(mockBookService.getBookByIsbn(anyString())).thenAnswer(
        a -> new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com", "A book"));
    when(mockRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class,
        () -> userService.addBookToUser("UID", "1234567890"));
  }

  @Test
  @DisplayName("addBookToUser throws NoSuchElementException when provided an ISBN that cannot be retrieved")
  void test_addBookToUser_invalidIsbn() {
    when(mockBookService.getBookByIsbn(anyString())).thenThrow(NoSuchElementException.class);

    assertThrows(NoSuchElementException.class,
        () -> userService.addBookToUser("UID", "1234567890"));
  }

  @Test
  @DisplayName("addBookToUser throws EntityExistsException when book already exists in user library")
  void test_addBookToUser_bookAlreadyPresent() {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    Book book = new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com", "A book");
    user.getBooks().add(book);
    when(mockBookService.getBookByIsbn(anyString())).thenReturn(book);
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));

    assertThrows(EntityExistsException.class, () -> userService.addBookToUser("UID", "1234567890"));
  }

  // tests for REMOVE BOOK FROM USER

  @Test
  @DisplayName("removeBookFromUser successfully removes a book from the user's library")
  void testRemoveBookFromUser_Success() {
    // Arrange
    User user = new User("user1", "John Doe", "john.doe@example.com", Region.NORTH_WEST,
        "http://thumbnail.com");
    Book book =
        new Book("9781234567897", "Title", "Author", "2021", "http://thumbnail.com", "Description");
    user.getBooks().add(book); // Add the book to the user's library

    when(mockRepository.findById("user1")).thenReturn(Optional.of(user)); // Mock user retrieval

    userService.removeBookFromUser("user1", "9781234567897");

    assertTrue(user.getBooks().isEmpty(), "The book should be removed from the user's library"); // Validate
                                                                                                 // book
                                                                                                 // removal
    verify(mockRepository, times(1)).save(user); // verify that the user was saved
  }


  @Test
  @DisplayName("removeBookFromUser throws NoSuchElementException if the user does not exist")
  void testRemoveBookFromUser_UserNotFound() {

    when(mockRepository.findById("user1")).thenReturn(Optional.empty()); // Mock user not found

    assertThrows(NoSuchElementException.class,
        () -> userService.removeBookFromUser("user1", "9781234567897"));
  }

  @Test
  @DisplayName("removeBookFromUser throws NoSuchElementException if the book is not in the user's library")
  void testRemoveBookFromUser_BookNotFound() {

    User user = new User("user1", "John Doe", "john.doe@example.com", Region.NORTH_WEST,
        "http://thumbnail.com");
    when(mockRepository.findById("user1")).thenReturn(Optional.of(user)); // Mock user exists but
                                                                          // has no books

    assertThrows(NoSuchElementException.class,
        () -> userService.removeBookFromUser("user1", "9781234567897"));
  }


  @Test
  @DisplayName("updateUserDetails throws NoSuchElementException if given UID that does not exist in repository")
  void test_updateUserDetails_userNotFound() {
    when(mockRepository.findById(anyString())).thenReturn(Optional.empty());
    Map<String, Object> emptyMap = setupMockAndReturnEmptyMap();

    assertThrows(NoSuchElementException.class,
            () -> userService.updateUserDetails("UID_1", emptyMap));
  }

  private Map<String, Object> setupMockAndReturnEmptyMap() {
    when(mockRepository.findById(anyString())).thenReturn(Optional.empty());
    return new HashMap<>();
  }

  @Test
  @DisplayName("When provided with a valid UID and an empty hashmap, an unchanged user entity is saved")
  void test_updateUserDetails_noUpdate() {
    User user = new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));

    when(mockRepository.save(any(User.class))).thenAnswer(a -> {
      User savedUser = a.getArgument(0);
      return new User(savedUser.getUid(), savedUser.getName(), savedUser.getEmail(),
          savedUser.getRegion(), savedUser.getThumbnail());
    });

    User changedUser = userService.updateUserDetails("UID_1", new HashMap<>());

    assertAll(() -> assertEquals(user.getUid(), changedUser.getUid()),
        () -> assertEquals(user.getName(), changedUser.getName()),
        () -> assertEquals(user.getEmail(), changedUser.getEmail()),
        () -> assertEquals(user.getRegion(), changedUser.getRegion()),
        () -> assertEquals(user.getThumbnail(), changedUser.getThumbnail()));
  }

  @Test
  @DisplayName("When provided with a valid UID and a hashmap with an invalid key, updateUserDetails throws IllegalArgumentException")
  void test_updateUserDetails_badUpdate() {
    User user = new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));

    when(mockRepository.save(any(User.class))).thenAnswer(a -> {
      User savedUser = a.getArgument(0);
      return new User(savedUser.getUid(), savedUser.getName(), savedUser.getEmail(),
          savedUser.getRegion(), savedUser.getThumbnail());
    });

    HashMap<String, Object> badMap = new HashMap<>();
    badMap.put("invalid", "invalid");

    assertThrows(IllegalArgumentException.class,
        () -> userService.updateUserDetails("UID_1", badMap));
  }

  @Test
  @DisplayName("When provided with a valid UID and a valid update hashmap, updateUserDetails updates all fields correctly")
  void test_updateUserDetails_validUpdate() {
    User user = new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1");
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));

    when(mockRepository.save(any(User.class))).thenAnswer(a -> {
      User savedUser = a.getArgument(0);
      return new User(savedUser.getUid(), savedUser.getName(), savedUser.getEmail(),
          savedUser.getRegion(), savedUser.getThumbnail());
    });

    HashMap<String, Object> updateMap = new HashMap<>();
    updateMap.put("name", "NEW NAME");
    updateMap.put("email", "NEW EMAIL");
    updateMap.put("region", Region.LONDON);
    updateMap.put("thumbnail", "NEW THUMB");

    User changedUser = userService.updateUserDetails("UID_1", updateMap);

    assertAll(() -> assertEquals("UID_1", changedUser.getUid()),
        () -> assertEquals("NEW NAME", changedUser.getName()),
        () -> assertEquals("NEW EMAIL", changedUser.getEmail()),
        () -> assertEquals(Region.LONDON, changedUser.getRegion()),
        () -> assertEquals("NEW THUMB", changedUser.getThumbnail()));
  }

  @Test
  @DisplayName("updateUserDetails throws IllegalArgumentException for null updates map")
  void test_updateUserDetails_nullUpdates() {
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(
            new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com")));

    assertThrows(IllegalArgumentException.class, () -> userService.updateUserDetails("UID", null));
  }

  @Test
  @DisplayName("updateUserDetails throws IllegalArgumentException for invalid region type")
  void test_updateUserDetails_invalidRegionType() {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    when(mockRepository.findById(anyString())).thenReturn(Optional.of(user));

    Map<String, Object> updates = new HashMap<>();
    updates.put("region", 12345); // Invalid type

    assertThrows(IllegalArgumentException.class, () -> userService.updateUserDetails("UID", updates));
  }



  @Test
  @DisplayName("isValidUser returns false for null user")
  void test_isValidUser_nullUser() {
    assertFalse(userService.isValidUser(null));
  }

  @Test
  @DisplayName("isValidUser returns false for user with missing fields")
  void test_isValidUser_missingFields() {
    User user = new User(null, "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    assertFalse(userService.isValidUser(user));

    user = new User("UID", null, "Email", Region.NORTH_WEST, "http://thumbnail.com");
    assertFalse(userService.isValidUser(user));
  }

  @Test
  @DisplayName("isValidUser returns true for fully populated user")
  void test_isValidUser_validUser() {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    assertTrue(userService.isValidUser(user));
  }

}
