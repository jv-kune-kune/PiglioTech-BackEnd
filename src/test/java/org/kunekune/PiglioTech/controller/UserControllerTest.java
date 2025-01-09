package org.kunekune.PiglioTech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService mockService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void Setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(userController)
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request with UID path variable to /api/v1/users/{uid} returns User details and HTTP 200 when UID exists")
    void test_getByUid_valid() throws Exception {
        User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");

        when(mockService.getUserByUid(anyString())).thenReturn(user);

        mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/users/12345")
        ).andExpect(MockMvcResultMatchers.status().isOk())
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

        mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/users/12345")
                ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("GET request with UID path variable to /api/v1/users/{uid} returns HTTP 400 when supplied UID is empty")
    void test_getByUid_noUid() throws Exception {
        mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/users/")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



    @Test
    @DisplayName("POST request with valid user details in request body returns those same details and HTTP 201")
    void test_post_validUser() throws Exception {
        User user = new User("UID", "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com");
        String json = mapper.writeValueAsString(user);

        when(mockService.saveUser(any(User.class))).thenAnswer(a -> {
            User submittedUser = a.getArgument(0);
            return new User(submittedUser.getUid(), submittedUser.getName(), submittedUser.getEmail(), submittedUser.getRegion(), submittedUser.getThumbnail());
        });

        mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uid").value(user.getUid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.region").value(user.getRegion().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value(user.getThumbnail()));
    }

    @Test
    @DisplayName("POST request with invalid user details in request body returns HTTP 400")
    void test_post_invalidUsers() throws Exception {
        String json_missingUid = mapper.writeValueAsString(new User(null, "Name", "Email", Region.NORTH_WEST, "http://thumbnail.com"));
        String json_missingName = mapper.writeValueAsString(new User("UID", null, "Email", Region.NORTH_WEST, "http://thumbnail.com"));
        String json_missingEmail = mapper.writeValueAsString(new User("UID", "Name", null, Region.NORTH_WEST, "http://thumbnail.com"));
        String json_missingRegion = mapper.writeValueAsString(new User("UID", "Name", "Email", null, "http://thumbnail.com"));
        String json_missingThumbnail = mapper.writeValueAsString(new User("UID", "Name", "Email", Region.NORTH_WEST, null));

        mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .content(json_missingUid)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .content(json_missingName)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .content(json_missingEmail)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .content(json_missingRegion)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
                        .content(json_missingThumbnail)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("POST request with absent user details in request body returns HTTP 400")
    void test_post_noBody() throws Exception {
        mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/users")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
