package org.kunekune.piglioteque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.piglioteque.exception.GlobalExceptionHandler;
import org.kunekune.piglioteque.model.*;
import org.kunekune.piglioteque.service.SwapService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class SwapControllerTest {
    private final static String endpoint = "/api/v1/swaps";

    @Mock
    private SwapService mockSwapService;

    @InjectMocks
    private SwapController swapController;

    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void Setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(swapController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request to /api/v1/swaps with valid user ID as parameter returns HTTP 200 and list of matches containing that user")
    void test_getSwaps_validId() throws Exception {
        MatchDto dto = new MatchDto(1L,
                new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1"),
                new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2"),
                new Book("12345", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3", "There can only be one"),
                new Book("54321", "TITLE 2", "AUTHOR 2", "1900", "http://thumbnail.com/4", "Two peas in a pod")
        );

        when(mockSwapService.getMatches(anyString())).thenReturn(List.of(dto));

        mockMvcController.perform(
                        MockMvcRequestBuilders.get(endpoint)
                                .param("userId", "UID_2")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userOne").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userTwo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userOneBook").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userTwoBook").exists());
    }

    @Test
    @DisplayName("GET request to /api/v1/swaps with invalid user ID as parameter returns HTTP 200 and empty list")
    void test_getSwaps_invalidId() throws Exception {
        when(mockSwapService.getMatches(anyString())).thenReturn(List.of());

        mockMvcController.perform(
                        MockMvcRequestBuilders.get(endpoint)
                                .param("userId", "UID_2")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET request to /api/v1/swaps with no parameter returns HTTP 400")
    void test_getSwaps_NoId() throws Exception {
        when(mockSwapService.getMatches(anyString())).thenReturn(List.of());

        mockMvcController.perform(
                MockMvcRequestBuilders.get(endpoint)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @DisplayName("POST request to /api/v1/swaps with valid SwapRequest body returns HTTP 201 and swap request details")
    void test_postSwaps_valid() throws Exception {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");
        String dtoBody = mapper.writeValueAsString(dto);

        when(mockSwapService.makeSwapRequest(any(SwapRequestDto.class))).thenAnswer(a -> {
            SwapRequest request = new SwapRequest(
                    new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1"),
                    new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2"),
                    new Book("12345", "TITLE 1", "AUTHOR 1", "1900", "http://thumbnail.com/3", "There can only be one")
            );
            request.setId(1L);
            return request;
        });

        mockMvcController.perform(
                        MockMvcRequestBuilders.post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoBody)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.initiator").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiver").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiverBook").exists());
    }

    // Red on Swap branch, throws exception that is definitely caught in Main
    @Test
    @DisplayName("POST request to /api/v1/swaps with SwapRequest identical to one sent before returns HTTP 409")
    void test_postSwaps_conflict() throws Exception {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");
        String dtoBody = mapper.writeValueAsString(dto);

        doThrow(new EntityExistsException("Identical swap request has already been made"))
                .when(mockSwapService).makeSwapRequest(any(SwapRequestDto.class));

        mockMvcController.perform(
                MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoBody)
        ).andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("POST request to /api/v1/swaps with IDs/ISBN not present in repository returns HTTP 404")
    void test_postSwaps_invalidIds() throws Exception {
        SwapRequestDto dto = new SwapRequestDto("UID_1", "UID_2", "12345");
        String dtoBody = mapper.writeValueAsString(dto);

        when(mockSwapService.makeSwapRequest(any(SwapRequestDto.class))).thenThrow(NoSuchElementException.class);

        mockMvcController.perform(
                MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoBody)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    // tests for SwapRequestDto
    @Test
    @DisplayName("POST request to /api/v1/swaps with invalid SwapRequestDto returns HTTP 400")
    void test_createSwap_invalidFields() throws Exception {
        // Invalid DTO with empty fields
        SwapRequestDto invalidDto = new SwapRequestDto("", "", "");
        String invalidDtoJson = mapper.writeValueAsString(invalidDto);

        mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/swaps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDtoJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("POST request to /api/v1/swaps with duplicate SwapRequestDto returns HTTP 409")
    void test_createSwap_duplicateRequest() throws Exception {
        SwapRequestDto duplicateDto = new SwapRequestDto("UID_1", "UID_2", "ISBN_123");
        String duplicateDtoJson = mapper.writeValueAsString(duplicateDto);

        doThrow(new EntityExistsException("Exception message")).when(mockSwapService)
                        .makeSwapRequest(any(SwapRequestDto.class));

        mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/swaps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateDtoJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("POST request to /api/v1/swaps with valid SwapRequestDto returns HTTP 201")
    void test_createSwap_valid() throws Exception {
        SwapRequestDto validDto = new SwapRequestDto("UID_1", "UID_2", "ISBN_123");
        String validDtoJson = mapper.writeValueAsString(validDto);

        when(mockSwapService.makeSwapRequest(any(SwapRequestDto.class))).thenAnswer(a -> {
            SwapRequest request = new SwapRequest(
                    new User("UID_1", "NAME 1", "EMAIL 1", Region.NORTH_WEST, "http://thumbnail.com/1"),
                    new User("UID_2", "NAME 2", "EMAIL 2", Region.NORTH_WEST, "http://thumbnail.com/2"),
                    new Book("ISBN_123", "TITLE 1", "AUTHOR 1", "2000", "http://thumbnail.com/book1", "Description")
            );
            request.setId(1L);
            return request;
        });

        mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/swaps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.initiator").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiver").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiverBook").exists());
    }


    @Test
    @DisplayName("POST request to /api/v1/swaps/dismiss with valid dismissal object returns HTTP 204")
    void test_postDismiss_validDismissal() throws Exception {
        SwapDismissal dismissal = new SwapDismissal("UID_1", 1L);
        String dismissalBody = mapper.writeValueAsString(dismissal);

        mockMvcController.perform(
                MockMvcRequestBuilders.post(endpoint + "/dismiss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dismissalBody)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("POST request to /api/v1/swaps/dismiss with dismissal object containing IDs not present returns HTTP 404")
    void test_postDismiss_notFound() throws Exception {
        SwapDismissal dismissal = new SwapDismissal("UID_1", 1L);
        String dismissalBody = mapper.writeValueAsString(dismissal);

        doThrow(new NoSuchElementException("Either ID or UID not present in any matches"))
                .when(mockSwapService).dismissSwap(any(SwapDismissal.class));

        mockMvcController.perform(
                MockMvcRequestBuilders.post(endpoint + "/dismiss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dismissalBody)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
