package org.kunekune.PiglioTech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.exception.GlobalExceptionHandler;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.IsbnDto;
import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

  @Mock
  private UserService mockService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvcController;

  private ObjectMapper mapper;

  private static final String END_POINT = "/api/v1/users";

  @BeforeEach
  public void Setup() {
    mockMvcController = MockMvcBuilders.standaloneSetup(userController)
        .setControllerAdvice(new GlobalExceptionHandler()).build();
    mapper = new ObjectMapper();
  }

  @Test
  @DisplayName("GET request with UID path variable to /api/v1/users/{uid} returns User details and HTTP 200 when UID exists")
  void test_getByUid_valid() throws Exception {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");

    when(mockService.getUserByUid(anyString())).thenReturn(user);

    mockMvcController.perform(MockMvcRequestBuilders.get(END_POINT + "/12345"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.uid").value(user.getUid()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.region").value(user.getRegion().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value(user.getThumbnail()));
  }

  @Test
  @DisplayName("GET request with UID path variable to /api/v1/users/{uid} returns HTTP 404 when UID does not exist")
  void test_getByUid_invalid() throws Exception {
    when(mockService.getUserByUid(anyString())).thenThrow(NoSuchElementException.class);

    mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/users/12345"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }



  @Test
  @DisplayName("POST request with valid user details in request body returns those same details and HTTP 201")
  void test_post_validUser() throws Exception {
    User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    String json = mapper.writeValueAsString(user);

    when(mockService.saveUser(any(User.class))).thenAnswer(a -> {
      User submittedUser = a.getArgument(0);
      return new User(submittedUser.getUid(), submittedUser.getName(), submittedUser.getEmail(),
          submittedUser.getRegion(), submittedUser.getThumbnail());
    });

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT).contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.uid").value(user.getUid()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.region").value(user.getRegion().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value(user.getThumbnail()));
  }

  @Test
  @DisplayName("POST request with invalid user details in request body returns HTTP 400")
  void test_post_invalidUsers() throws Exception {
    String jsonMissingUid = mapper.writeValueAsString(
        new User(null, "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com"));
    String jsonMissingName = mapper.writeValueAsString(
        new User("UID", null, "Email", Region.NORTH_WEST, "http://thumbnail.com"));
    String jsonMissingEmail = mapper.writeValueAsString(
        new User("UID", "Name", null, Region.NORTH_WEST, "http://thumbnail.com"));
    String jsonMissingRegion =
        mapper.writeValueAsString(new User("UID", "Name", "Email", null, "http://thumbnail.com"));
    String jsonMissingThumbnail =
        mapper.writeValueAsString(new User("UID", "Name", "Email", Region.NORTH_WEST, null));

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT).content(jsonMissingUid)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT).content(jsonMissingName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT).content(jsonMissingEmail)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT).content(jsonMissingRegion)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT).content(jsonMissingThumbnail)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("POST request with absent user details in request body returns HTTP 400")
  void test_post_noBody() throws Exception {
    mockMvcController.perform(MockMvcRequestBuilders.post(END_POINT))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET request with no parameters to /api/v1/users returns all users with HTTP 200")
  void test_getAllUsers_noParameters() throws Exception {
    List<User> users = List.of(
        new User("1", "Alice", "alice@example.com", Region.NORTH_WEST,
            "http://example.com/thumbnail1.jpg"),
        new User("2", "Bob", "bob@example.com", Region.SOUTH_EAST,
            "http://example.com/thumbnail2.jpg"));

    when(mockService.getAllUsers()).thenReturn(users);

    mockMvcController.perform(MockMvcRequestBuilders.get(END_POINT))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].uid").value(users.get(0).getUid()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(users.get(0).getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].uid").value(users.get(1).getUid()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(users.get(1).getName()));
  }

  @Test
  @DisplayName("GET request to /api/v1/users returns empty list and HTTP 200 when no users are found")
  void test_getAllUsers_emptyList() throws Exception {
    when(mockService.getAllUsers()).thenReturn(List.of());

    mockMvcController.perform(MockMvcRequestBuilders.get(END_POINT))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName("GET request with valid region to /api/v1/users returns list of all users in that region and HTTP 200")
  void test_getRegion_validRegion() throws Exception {
    User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user3 = new User("UID_3", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    List<User> users = List.of(user1, user2, user3, user4, user5);

    when(mockService.getUsersByRegion(any(Region.class))).thenReturn(users);

    mockMvcController.perform(MockMvcRequestBuilders.get(END_POINT).param("region", "NORTH_WEST"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5)));
  }

  @Test
  @DisplayName("GET request with invalid region to /api/v1/users returns HTTP 400")
  void test_getRegion_invalidRegion() throws Exception {
    when(mockService.getUsersByRegion(any(Region.class))).thenReturn(List.of());

    mockMvcController.perform(MockMvcRequestBuilders.get(END_POINT).param("region", "north_west"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("GET request with valid region and UID string to /api/v1/users returns list of users and HTTP 200")
  void test_getRegion_validRegion_excludeString() throws Exception {
    User user1 = new User("UID_1", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user2 = new User("UID_2", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user4 = new User("UID_4", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    User user5 = new User("UID_5", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
    List<User> users = List.of(user1, user2, user4, user5);

    when(mockService.getUsersByRegionExclude(any(Region.class), anyString())).thenReturn(users);

    mockMvcController
        .perform(MockMvcRequestBuilders.get(END_POINT).param("region", "NORTH_WEST").param("exclude",
            "UID_3"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
  }

  @Test
  @DisplayName("GET request with invalid region and UID string to /api/v1/users returns HTTP 400")
  void test_getRegion_invalidRegion_excludeString() throws Exception {
    when(mockService.getUsersByRegionExclude(any(Region.class), anyString())).thenReturn(List.of());

    mockMvcController.perform(MockMvcRequestBuilders.get(END_POINT).param("region", "north_west")
        .param("exclude", "UID_3")).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }



  @Test
  @DisplayName("POST request to /api/v1/users/{id}/books with valid user ID and valid request body returns updated user details and HTTP 201")
  void test_addBookToUser_validUser_validIsbn() throws Exception {
    User user = new User("98765", "NAME", "EMAIL", Region.NORTH_WEST, "http://thumbnail.com/0");
    Book book =
        new Book("1234567890", "TITLE", "AUTHOR", "1900", "http://thumbnail.com/1", "A book");
    String isbn = mapper.writeValueAsString(new IsbnDto("1234567890"));
    when(mockService.addBookToUser(anyString(), anyString())).thenAnswer(a -> {
      user.getBooks().add(book);
      return user;
    });

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT + "/98765/books")
            .contentType(MediaType.APPLICATION_JSON).content(isbn))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.books", hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.books[0].isbn").value(book.getIsbn()));
  }

  @Test
  @DisplayName("POST request to /api/v1/users/{id}/books with invalid user ID returns HTTP 404")
  void test_addBookToUser_invalidUser() throws Exception {
    String isbn = mapper.writeValueAsString(new IsbnDto("1234567890"));

    when(mockService.addBookToUser(anyString(), anyString()))
        .thenThrow(NoSuchElementException.class);

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT + "/12345/books")
            .contentType(MediaType.APPLICATION_JSON).content(isbn))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DisplayName("POST request to /api/v1/users/{id}/books with invalid request body returns HTTP 400")
  void test_addBookToUser_invalidContent() throws Exception {
    String isbn = "{\"not-isbn\": \"1234567890\"}";

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT + "/12345/books")
            .contentType(MediaType.APPLICATION_JSON).content(isbn))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("POST request to /api/v1/users/{id}/books with ISBN already associated with user returns HTTP 409")
  void test_addBookToUser_conflictingIsbns() throws Exception {
    String isbn = mapper.writeValueAsString(new IsbnDto("1234567890"));

    doThrow(new EntityExistsException("Error message")).when(mockService).addBookToUser(anyString(),
        anyString());

    mockMvcController
        .perform(MockMvcRequestBuilders.post(END_POINT + "/98765/books")
            .contentType(MediaType.APPLICATION_JSON).content(isbn))
        .andExpect(MockMvcResultMatchers.status().isConflict());
  }

  @Test
  @DisplayName("DELETE request to /api/v1/users/{id}/books/{isbn} with valid user ID and valid ISBN returns HTTP 204")
  void test_removeBookFromUser_validRequest() throws Exception {
    doNothing().when(mockService).removeBookFromUser(anyString(), anyString());

    mockMvcController.perform(MockMvcRequestBuilders.delete(END_POINT + "/12345/books/1234567890"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE request to /api/v1/users/{id}/books/{isbn} with either invalid user ID or ISBN returns HTTP 404")
  void test_removeBookFromUser_invalidIds() throws Exception {
    doThrow(NoSuchElementException.class).when(mockService).removeBookFromUser(anyString(),
        anyString());

    mockMvcController.perform(MockMvcRequestBuilders.delete(END_POINT + "/12345/books/1234567890"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
