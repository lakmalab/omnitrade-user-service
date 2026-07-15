package com.omnitrade.user_service.controller;

import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UpdateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import com.omnitrade.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;

    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;

    private UserResponseDTO userResponse;
    private UserResponseDTO updatedResponse;

    @BeforeEach
    void setUp() {

        userId = UUID.randomUUID();

        createRequest = CreateUserRequest.builder()
                .username("john_doe")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .phone("+1234567890")
                .city("New York")
                .district("Manhattan")
                .profileImage("profile.jpg")
                .build();

        updateRequest = UpdateUserRequest.builder()
                .id(userId)
                .username("john_updated")
                .email("john.updated@example.com")
                .firstName("Johnny")
                .lastName("Updated")
                .phone("+9876543210")
                .city("Los Angeles")
                .district("Hollywood")
                .profileImage("updated_profile.jpg")
                .build();

        userResponse = UserResponseDTO.builder()
                .username("john_doe")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .phone("+1234567890")
                .city("New York")
                .district("Manhattan")
                .profileImage("profile.jpg")
                .build();

        updatedResponse = UserResponseDTO.builder()
                .username("john_updated")
                .email("john.updated@example.com")
                .firstName("Johnny")
                .lastName("Updated")
                .phone("+9876543210")
                .city("Los Angeles")
                .district("Hollywood")
                .profileImage("updated_profile.jpg")
                .build();
    }

    @Test
    void createUser_ShouldReturnCreatedResponse() {

        when(userService.createUser(createRequest)).thenReturn(userResponse);

        ResponseEntity<UserResponseDTO> response =
                userController.createUser(createRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("john_doe", response.getBody().getUsername());
        assertEquals("john.doe@example.com", response.getBody().getEmail());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());

        verify(userService).createUser(createRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getProfile_ShouldReturnUser() {

        when(userService.getProfile(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponseDTO> response =
                userController.getProfile(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(userResponse, response.getBody());

        verify(userService).getProfile(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void updateProfile_ShouldReturnUpdatedUser() {

        when(userService.updateProfile(updateRequest))
                .thenReturn(updatedResponse);

        ResponseEntity<UserResponseDTO> response =
                userController.updateProfile(updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(updatedResponse, response.getBody());

        verify(userService).updateProfile(updateRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllProfiles_ShouldReturnList() {

        List<UserResponseDTO> users =
                List.of(userResponse, updatedResponse);

        when(userService.getAllProfiles()).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response =
                userController.getAllProfiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals("john_doe",
                response.getBody().get(0).getUsername());

        assertEquals("john_updated",
                response.getBody().get(1).getUsername());

        verify(userService).getAllProfiles();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllProfiles_ShouldReturnEmptyList() {

        when(userService.getAllProfiles()).thenReturn(List.of());

        ResponseEntity<List<UserResponseDTO>> response =
                userController.getAllProfiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(userService).getAllProfiles();
    }

    @Test
    void createUser_ShouldPropagateException() {

        RuntimeException exception =
                new RuntimeException("Database Error");

        when(userService.createUser(createRequest))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userController.createUser(createRequest)
        );

        assertEquals("Database Error", thrown.getMessage());

        verify(userService).createUser(createRequest);
    }

    @Test
    void getProfile_ShouldPropagateException() {

        RuntimeException exception =
                new RuntimeException("User Not Found");

        when(userService.getProfile(userId))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userController.getProfile(userId)
        );

        assertEquals("User Not Found", thrown.getMessage());

        verify(userService).getProfile(userId);
    }

    @Test
    void updateProfile_ShouldPropagateException() {

        RuntimeException exception =
                new RuntimeException("Update Failed");

        when(userService.updateProfile(updateRequest))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userController.updateProfile(updateRequest)
        );

        assertEquals("Update Failed", thrown.getMessage());

        verify(userService).updateProfile(updateRequest);
    }

    @Test
    void getAllProfiles_ShouldPropagateException() {

        RuntimeException exception =
                new RuntimeException("Database Error");

        when(userService.getAllProfiles())
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userController.getAllProfiles()
        );

        assertEquals("Database Error", thrown.getMessage());

        verify(userService).getAllProfiles();
    }
}