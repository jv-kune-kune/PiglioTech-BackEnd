package org.kunekune.PiglioTech.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
}