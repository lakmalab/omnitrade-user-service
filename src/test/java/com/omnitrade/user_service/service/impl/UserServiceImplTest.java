package com.omnitrade.user_service.service.impl;

import com.omnitrade.user_service.exception.DuplicateEmailException;
import com.omnitrade.user_service.exception.UserNotFoundException;
import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UpdateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import com.omnitrade.user_service.model.entity.UserProfile;
import com.omnitrade.user_service.model.enums.UserRole;
import com.omnitrade.user_service.model.enums.UserStatus;
import com.omnitrade.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;
    private UserProfile userProfile;
    private UserProfile updatedUserProfile;

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

        userProfile = UserProfile.builder()
                .id(userId)
                .username("john_doe")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .phone("+1234567890")
                .city("New York")
                .district("Manhattan")
                .profileImage("profile.jpg")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .rating(0.0)
                .reviewCount(0)
                .totalSales(0)
                .totalTrades(0)
                .phoneVerified(false)
                .emailVerified(false)
                .joinedAt(Instant.now())
                .build();

        updatedUserProfile = UserProfile.builder()
                .id(userId)
                .username("john_updated")
                .email("john.updated@example.com")
                .firstName("Johnny")
                .lastName("Updated")
                .phone("+9876543210")
                .city("Los Angeles")
                .district("Hollywood")
                .profileImage("updated_profile.jpg")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .rating(0.0)
                .reviewCount(0)
                .totalSales(0)
                .totalTrades(0)
                .phoneVerified(false)
                .emailVerified(false)
                .joinedAt(Instant.now())
                .build();
    }

    @Test
    void createUser_ShouldSucceed_WhenEmailIsUnique() {
        // Given
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // When
        UserResponseDTO result = userService.createUser(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john_doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getPhone()).isEqualTo("+1234567890");
        assertThat(result.getCity()).isEqualTo("New York");
        assertThat(result.getDistrict()).isEqualTo("Manhattan");
        assertThat(result.getProfileImage()).isEqualTo("profile.jpg");

        verify(userRepository).existsByEmail(createRequest.getEmail());
        verify(userRepository).save(any(UserProfile.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        // Given
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining(createRequest.getEmail());

        verify(userRepository).existsByEmail(createRequest.getEmail());
        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void createUser_ShouldTrimUsername_WhenUsernameHasWhitespace() {
        // Given
        CreateUserRequest requestWithWhitespace = CreateUserRequest.builder()
                .username("  john_doe  ")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userRepository.existsByEmail(requestWithWhitespace.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // When
        userService.createUser(requestWithWhitespace);

        // Then
        verify(userRepository).save(argThat(userProfile ->
                userProfile.getUsername().equals("john_doe")
        ));
    }

    @Test
    void createUser_ShouldHandleNullEmail() {
        // Given
        CreateUserRequest requestWithNullEmail = CreateUserRequest.builder()
                .username("john_doe")
                .email(null)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // When
        userService.createUser(requestWithNullEmail);

        // Then
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository).save(any(UserProfile.class));
    }

    @Test
    void getProfile_ShouldSucceed_WhenUserExists() {
        // Given
        when(userRepository.getById(userId)).thenReturn(userProfile);

        // When
        UserResponseDTO result = userService.getProfile(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john_doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");

        verify(userRepository).getById(userId);
    }

    @Test
    void getProfile_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.getById(userId)).thenThrow(new RuntimeException("User not found"));

        // When & Then
        assertThatThrownBy(() -> userService.getProfile(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Failed to Find User with ID: " + userId);

        verify(userRepository).getById(userId);
    }

    @Test
    void getAllProfiles_ShouldSucceed_WhenUsersExist() {
        // Given
        List<UserProfile> users = Arrays.asList(userProfile, updatedUserProfile);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponseDTO> result = userService.getAllProfiles();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserResponseDTO::getUsername)
                .containsExactly("john_doe", "john_updated");

        verify(userRepository).findAll();
    }

    @Test
    void getAllProfiles_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserResponseDTO> result = userService.getAllProfiles();

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findAll();
    }

    @Test
    void getAllProfiles_ShouldThrowException_WhenDataAccessExceptionOccurs() {
        // Given
        when(userRepository.findAll()).thenThrow(new DataAccessException("Database error") {});

        // When & Then
        assertThatThrownBy(() -> userService.getAllProfiles())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to fetch user profiles");

        verify(userRepository).findAll();
    }

    @Test
    void getAllProfiles_ShouldThrowException_WhenUnexpectedExceptionOccurs() {
        // Given
        when(userRepository.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        assertThatThrownBy(() -> userService.getAllProfiles())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("An unexpected error occurred while fetching user profiles");

        verify(userRepository).findAll();
    }

    @Test
    void updateProfile_ShouldSucceed_WhenUserExists() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(userProfile));
        when(userRepository.save(any(UserProfile.class))).thenReturn(updatedUserProfile);

        // When
        UserResponseDTO result = userService.updateProfile(updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john_updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(result.getFirstName()).isEqualTo("Johnny");
        assertThat(result.getLastName()).isEqualTo("Updated");
        assertThat(result.getPhone()).isEqualTo("+9876543210");
        assertThat(result.getCity()).isEqualTo("Los Angeles");
        assertThat(result.getDistrict()).isEqualTo("Hollywood");
        assertThat(result.getProfileImage()).isEqualTo("updated_profile.jpg");

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(UserProfile.class));
    }

    @Test
    void updateProfile_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateProfile(updateRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with ID: " + userId);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void updateProfile_ShouldKeepExistingValues_WhenRequestFieldsAreNull() {
        // Given
        UpdateUserRequest partialUpdateRequest = UpdateUserRequest.builder()
                .id(userId)
                .username(null)
                .email(null)
                .firstName(null)
                .lastName(null)
                .phone(null)
                .city(null)
                .district(null)
                .profileImage(null)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userProfile));
        when(userRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // When
        UserResponseDTO result = userService.updateProfile(partialUpdateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userProfile.getUsername());
        assertThat(result.getEmail()).isEqualTo(userProfile.getEmail());
        assertThat(result.getFirstName()).isEqualTo(userProfile.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userProfile.getLastName());
        assertThat(result.getPhone()).isEqualTo(userProfile.getPhone());
        assertThat(result.getCity()).isEqualTo(userProfile.getCity());
        assertThat(result.getDistrict()).isEqualTo(userProfile.getDistrict());
        assertThat(result.getProfileImage()).isEqualTo(userProfile.getProfileImage());

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getUsername().equals(userProfile.getUsername()) &&
                        savedUser.getEmail().equals(userProfile.getEmail())
        ));
    }

    @Test
    void updateProfile_ShouldTrimUsername_WhenUsernameProvided() {
        // Given
        UpdateUserRequest requestWithWhitespace = UpdateUserRequest.builder()
                .id(userId)
                .username("  updated_username  ")
                .build();

        UserProfile expectedSavedProfile = UserProfile.builder()
                .id(userId)
                .username("updated_username")
                .email(userProfile.getEmail())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .phone(userProfile.getPhone())
                .city(userProfile.getCity())
                .district(userProfile.getDistrict())
                .profileImage(userProfile.getProfileImage())
                .role(userProfile.getRole())
                .status(userProfile.getStatus())
                .rating(userProfile.getRating())
                .reviewCount(userProfile.getReviewCount())
                .totalSales(userProfile.getTotalSales())
                .totalTrades(userProfile.getTotalTrades())
                .phoneVerified(userProfile.getPhoneVerified())
                .emailVerified(userProfile.getEmailVerified())
                .joinedAt(userProfile.getJoinedAt())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userProfile));
        when(userRepository.save(any(UserProfile.class))).thenReturn(expectedSavedProfile);

        // When
        userService.updateProfile(requestWithWhitespace);

        // Then
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getUsername().equals("updated_username")
        ));
    }
    @Test
    void mapToResponseDTO_ShouldMapAllFieldsCorrectly() {
        // Given
        // You need to mock the repository call for this test too
        when(userRepository.getById(userId)).thenReturn(userProfile);

        // When
        UserResponseDTO result = userService.getProfile(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userProfile.getUsername());
        assertThat(result.getEmail()).isEqualTo(userProfile.getEmail());
        assertThat(result.getFirstName()).isEqualTo(userProfile.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userProfile.getLastName());
        assertThat(result.getPhone()).isEqualTo(userProfile.getPhone());
        assertThat(result.getCity()).isEqualTo(userProfile.getCity());
        assertThat(result.getDistrict()).isEqualTo(userProfile.getDistrict());
        assertThat(result.getProfileImage()).isEqualTo(userProfile.getProfileImage());
    }
}