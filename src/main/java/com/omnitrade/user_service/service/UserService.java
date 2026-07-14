package com.omnitrade.user_service.service;

import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequest request);
}
