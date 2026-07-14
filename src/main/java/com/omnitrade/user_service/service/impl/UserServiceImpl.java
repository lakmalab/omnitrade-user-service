package com.omnitrade.user_service.service.impl;

import com.omnitrade.user_service.exception.DuplicateEmailException;
import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import com.omnitrade.user_service.model.entity.UserProfile;
import com.omnitrade.user_service.model.enums.UserRole;
import com.omnitrade.user_service.model.enums.UserStatus;
import com.omnitrade.user_service.repository.UserRepository;
import com.omnitrade.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
                .joinedAt(LocalDate.now())
                .build();
        UserProfile saved = repository.save(userProfile);

        log.info("User created successfully with ID: {}", saved);

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
