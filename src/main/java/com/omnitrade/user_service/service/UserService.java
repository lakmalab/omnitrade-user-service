package com.omnitrade.user_service.service;

import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UpdateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequest request);

    UserResponseDTO getProfile(@Valid UUID id);

    List<UserResponseDTO> getAllProfiles();

    UserResponseDTO updateProfile(@Valid UpdateUserRequest request);
}
