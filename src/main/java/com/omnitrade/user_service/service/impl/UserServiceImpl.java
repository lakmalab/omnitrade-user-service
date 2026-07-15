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
import com.omnitrade.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserResponseDTO createUser(CreateUserRequest request) {
        if (request.getEmail() != null && !request.getEmail().isEmpty() &&
                repository.existsByEmail(request.getEmail())) {
           throw new DuplicateEmailException(request.getEmail());
        }

        UserProfile userProfile = UserProfile.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .city(request.getCity())
                .district(request.getDistrict())
                .profileImage(request.getProfileImage())
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
        UserProfile saved = repository.save(userProfile);

        log.info("User created successfully with ID: {}", saved);

        return mapToResponseDTO(saved);
    }

    @Override
    public UserResponseDTO getProfile(UUID id) {
        try {
            UserProfile user = repository.getById(id);
            log.info("User found successfully with ID: {}", user);
            return mapToResponseDTO(user);
        } catch (RuntimeException e) {
            throw new UserNotFoundException("Failed to Find User with ID: " + id.toString());
        }
    }
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllProfiles() {
        log.info("Fetching all user profiles");

        try {
            List<UserProfile> users = repository.findAll();

            if (users.isEmpty()) {
                log.warn("No users found in the database");
                return Collections.emptyList();
            }

            log.debug("Found {} user profiles", users.size());
            return users.stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());

        } catch (DataAccessException e) {
            log.error("Database error while fetching all users: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user profiles", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching all users: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while fetching user profiles", e);
        }
    }

    @Override
    public UserResponseDTO updateProfile(UpdateUserRequest request) {
        log.info("Updating user profile with ID: {}", request.getId());

        UserProfile existingUser = repository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getId()));


        UserProfile updatedUser = UserProfile.builder()
                .id(existingUser.getId())
                .username(request.getUsername() != null ? request.getUsername().trim() : existingUser.getUsername())
                .email(request.getEmail() != null ? request.getEmail() : existingUser.getEmail())
                .firstName(request.getFirstName() != null ? request.getFirstName() : existingUser.getFirstName())
                .lastName(request.getLastName() != null ? request.getLastName() : existingUser.getLastName())
                .phone(request.getPhone() != null ? request.getPhone() : existingUser.getPhone())
                .city(request.getCity() != null ? request.getCity() : existingUser.getCity())
                .district(request.getDistrict() != null ? request.getDistrict() : existingUser.getDistrict())
                .profileImage(request.getProfileImage() != null ? request.getProfileImage() : existingUser.getProfileImage())
                .role(existingUser.getRole())
                .status(existingUser.getStatus())
                .rating(existingUser.getRating())
                .reviewCount(existingUser.getReviewCount())
                .totalSales(existingUser.getTotalSales())
                .totalTrades(existingUser.getTotalTrades())
                .phoneVerified(existingUser.getPhoneVerified())
                .emailVerified(existingUser.getEmailVerified())
                .joinedAt(existingUser.getJoinedAt())
                .build();

        UserProfile saved = repository.save(updatedUser);
        log.info("User profile updated successfully with ID: {}", saved.getId());

        return mapToResponseDTO(saved);
    }

    private UserResponseDTO mapToResponseDTO(UserProfile user) {
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .city(user.getCity())
                .district(user.getDistrict())
                .profileImage(user.getProfileImage())
                .build();
    }
}
