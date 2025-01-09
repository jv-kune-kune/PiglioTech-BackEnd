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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;

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
                .andExpect(MockMvcResultMatchers.jsonPath(".uid").value(user.getUid()))
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath(".region").value(user.getRegion().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath(".thumbnail").value(user.getThumbnail()));
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
}
