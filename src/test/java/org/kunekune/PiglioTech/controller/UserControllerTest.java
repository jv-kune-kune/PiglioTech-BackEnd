package org.kunekune.PiglioTech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.kunekune.PiglioTech.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
}
